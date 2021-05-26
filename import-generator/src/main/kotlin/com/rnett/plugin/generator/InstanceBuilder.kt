package com.rnett.plugin.generator

import com.rnett.plugin.ExportDeclaration
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec

//TODO I would like to be able to get the Resolved classes from an instance.  Not possible b/c it requires context.
//  But can I get rid of context property & use children filtering to find children
object InstanceBuilder {

    const val name: String = "Instance"

    //TODO do one w/ class wrapping an expression, to get properties, methods, etc?  Might be too complicated, would just be calling w/ receivers
    //     type as well?  i.e. raising + extracting type params
    fun buildInstance(className: ClassName, declaration: ExportDeclaration): Pair<List<TypeSpec>, List<FunSpec>> {
        val builder = TypeSpec.classBuilder(name)

        val builders = when (declaration) {
            is ExportDeclaration.Class -> declaration.buildClassInstance(builder, className)
            is ExportDeclaration.Constructor -> declaration.buildConstructorInstance(builder, className)
            is ExportDeclaration.Function -> declaration.buildFunctionInstance(builder, className)
            is ExportDeclaration.Property -> declaration.buildPropertyInstance(builder, className)
            is ExportDeclaration.Typealias -> declaration.buildTypealiasInstance(builder, className)
        }?.toMutableList() ?: return emptyList<TypeSpec>() to emptyList<FunSpec>()

        if (declaration is ExportDeclaration.Property) {
            builders += declaration.addAccessorInstanceGetters(className)
        }

        if (declaration is ExportDeclaration.Property) {
            return listOf(builder.build()) + declaration.additionalPropertyDeclarations(className.nestedClass(name)) to builders
        } else
            return listOf(builder.build()) to builders
    }

    private fun TypeSpec.Builder.addSignatureCheck(signature: CodeBlock, className: ClassName) {
        addInitializerBlock(
            CodeBlock.builder()
                .addStatement("val signature = %L", signature)
                .addStatement("val requiredSignature = %T.signature", className)
                .beginControlFlow("require(signature == requiredSignature)")
                .addStatement("%P", "Instance's signature \$signature did not match the required signature of \$requiredSignature")
                .endControlFlow()
                .build()
        )
    }

    private fun ExportDeclaration.Class.buildClassInstance(builder: TypeSpec.Builder, className: ClassName): List<FunSpec>? {
        return null
    }

    private fun builderMethods(
        name: String,
        argName: String,
        argType: TypeName,
        className: ClassName,
        signature: CodeBlock,
        instanceClassName: String = this.name,
        additionalCondition: CodeBlock = CodeBlock.of("")
    ): List<FunSpec> {
        val instanceClass = className.nestedClass(instanceClassName)
        return listOf(
            FunSpec.builder(name)
                .addParameter(argName, argType)
                .returns(instanceClass)
                .addStatement("return %T($argName)", instanceClass)
                .build(),
            FunSpec.builder("${name}OrNull")
                .addParameter(argName, argType)
                .returns(instanceClass.copy(nullable = true))
                .beginControlFlow("if(%L == %T.signature%L)", signature, className, additionalCondition)
                .addStatement("return %T($argName)", instanceClass)
                .endControlFlow()
                .beginControlFlow("else")
                .addStatement("return null")
                .endControlFlow()
                .build()
        )
    }

    private fun ExportDeclaration.Constructor.buildConstructorInstance(builder: TypeSpec.Builder, className: ClassName): List<FunSpec> {

        builder.primaryConstructor(FunSpec.constructorBuilder().addParameter("call", References.IrConstructorCall).build())
        builder.addProperty(PropertySpec.builder("call", References.IrConstructorCall).initializer("call").build())
        val getSignature = CodeBlock.of("call.symbol.signature")

        builder.addSignatureCheck(getSignature, className)

        builder.addTypeParameters(classTypeParams)
        builder.addValueParameters(valueParameters)

        return builderMethods("instance", "call", References.IrConstructorCall, className, getSignature)
    }

    private fun ExportDeclaration.Function.buildFunctionInstance(builder: TypeSpec.Builder, className: ClassName): List<FunSpec> {

        builder.primaryConstructor(FunSpec.constructorBuilder().addParameter("call", References.IrCall).build())
        builder.addProperty(PropertySpec.builder("call", References.IrCall).initializer("call").build())
        val getSignature = CodeBlock.of("call.symbol.signature")

        builder.addSignatureCheck(getSignature, className)

        builder.addTypeParameters(typeParameters)
        builder.addValueParameters(valueParameters)
        builder.addReceivers(dispatchReceiver, extensionReceivers)

        return builderMethods("instance", "call", References.IrCall, className, getSignature)
    }

    private fun ExportDeclaration.Property.buildPropertyInstance(builder: TypeSpec.Builder, className: ClassName): List<FunSpec> {

        builder.primaryConstructor(FunSpec.constructorBuilder().addParameter("call", References.IrCall).build())
        builder.addProperty(PropertySpec.builder("call", References.IrCall).initializer("call").build())
        builder.addModifiers(KModifier.OPEN)
        val getSignature = CodeBlock.of("call.symbol.owner.correspondingPropertySymbol?.signature")

        builder.addSignatureCheck(getSignature, className)

        builder.addTypeParameters(typeParameters)
        builder.addReceivers(dispatchReceiver, extensionReceivers)

        builder.addProperty(
            PropertySpec.builder("property", References.IrProperty)
                .getter(
                    FunSpec.getterBuilder()
                        .addStatement("return call.symbol.owner.correspondingPropertySymbol!!.owner")
                        .build()
                )
                .build()
        )

        return builderMethods("accessorInstance", "call", References.IrCall, className, getSignature)
    }

    private fun ExportDeclaration.Property.additionalPropertyDeclarations(instanceClass: ClassName): List<TypeSpec> {

        fun TypeSpec.Builder.common(isRightAccessor: MemberName, rightAccessorName: String) {
            superclass(instanceClass)
            primaryConstructor(FunSpec.constructorBuilder().addParameter("call", References.IrCall).build())
            addSuperclassConstructorParameter("call")
            addInitializerBlock(
                CodeBlock.builder()
                    .beginControlFlow("require(call.symbol.owner.%M)", isRightAccessor)
                    .addStatement("%P", "Instance \${call.symbol} is not the right type of accessor, expected a $rightAccessorName")
                    .endControlFlow()
                    .build()
            )
        }

        val getterInstance = if (hasGetter) {
            val builder = TypeSpec.classBuilder("Getter$name")
            builder.common(References.isGetter, "getter")

            builder.build()
        } else null

        val setterInstance = if (hasSetter) {
            val builder = TypeSpec.classBuilder("Setter$name")
            builder.common(References.isSetter, "setter")

            builder.addValueParameters(listOf(ExportDeclaration.Param("value", 0, false, false, valueType)))

            builder.build()
        } else null

        return listOfNotNull(getterInstance, setterInstance)
    }

    private fun ExportDeclaration.Property.addAccessorInstanceGetters(className: ClassName): List<FunSpec> {
        val getSignature = CodeBlock.of("call.symbol.owner.correspondingPropertySymbol?.signature")
        val getterInstances = if (hasGetter) {
            builderMethods(
                "getterInstance",
                "call",
                References.IrCall,
                className,
                getSignature,
                "Getter$name",
                CodeBlock.of(" && call.symbol.owner.%M", References.isGetter)
            )
        } else null

        val setterInstances = if (hasSetter) {
            builderMethods(
                "setterInstance",
                "call",
                References.IrCall,
                className,
                getSignature,
                "Setter$name",
                CodeBlock.of(" && call.symbol.owner.%M", References.isSetter)
            )
        } else null

        return getterInstances.orEmpty() + setterInstances.orEmpty()
    }

    private fun ExportDeclaration.Typealias.buildTypealiasInstance(builder: TypeSpec.Builder, className: ClassName): List<FunSpec> {
        return emptyList()
    }

    private fun TypeSpec.Builder.addTypeParameters(typeParameters: List<ExportDeclaration.TypeParameter>) {
        typeParameters.forEach {
            addProperty(
                PropertySpec.builder(it.name, References.IrType.copy(nullable = true))
                    .getter(FunSpec.getterBuilder().addStatement("return call.getTypeArgument(${it.index})").build())
                    .build()
            )
        }
    }

    private fun TypeSpec.Builder.addValueParameters(valueParameters: List<ExportDeclaration.Param>) {
        valueParameters.forEach {
            addProperty(
                PropertySpec.builder(it.name, References.IrExpression.copy(nullable = true))
                    .getter(FunSpec.getterBuilder().addStatement("return call.getValueArgument(${it.index})").build())
                    .build()
            )
        }
    }

    private fun TypeSpec.Builder.addReceivers(dispatchReceiver: ExportDeclaration.Receiver?, extensionReceivers: List<ExportDeclaration.Receiver>) {
        dispatchReceiver?.let {
            addProperty(
                PropertySpec.builder("dispatchReceiver", References.IrExpression.copy(nullable = true))
                    .getter(FunSpec.getterBuilder().addStatement("return call.dispatchReceiver").build())
                    .build()
            )
        }

        if (extensionReceivers.isNotEmpty()) {
            if (extensionReceivers.size > 1)
                error("Multiple extension receivers not supported yet")
            extensionReceivers.forEach {
                val name = "extensionReceiver"
                addProperty(
                    PropertySpec.builder(name, References.IrExpression.copy(nullable = true))
                        .getter(FunSpec.getterBuilder().addStatement("return call.%L", name).build())
                        .build()
                )
            }
        }
    }


}
