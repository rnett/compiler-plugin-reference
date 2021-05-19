package com.rnett.plugin.generator

import com.rnett.plugin.ExportDeclaration
import com.rnett.plugin.TypeString
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asTypeName

internal object ResolvedBuilder {
    fun build(builder: TypeSpec.Builder, current: ClassName, declarationTree: DeclarationTree) {
        builder.addContextProperty()

        if (declarationTree is DeclarationTree.Package) {
            builder.addContextConstructor()
        } // added in build otherwise

        declarationTree.declaration?.let { builder.build(it) }

        declarationTree.children.forEach {
            val type = current.nestedClass(it.displayName)
            builder.addFunction(
                FunSpec.builder(it.displayName)
                    .returns(type)
                    .addStatement("return %T(%L)", type, contextPropName)
                    .build()
            )
        }
    }

    private fun TypeSpec.Builder.build(declaration: ExportDeclaration) {
        val kdoc = CodeBlock.builder()
        kdoc.addStatement("Resolved reference to `${declaration.fqName.fqName}`")
        kdoc.add("\n")

        primaryConstructor(
            FunSpec.constructorBuilder()
                .addContextParameter()
                .addModifiers(KModifier.PRIVATE)
                .addParameter("symbol", References.symbolType(declaration))
                .build()
        )

        addFunction(
            FunSpec.constructorBuilder()
                .addParameter("context", References.IrPluginContext)
                .callThisConstructor("context", "resolveSymbol(context)")
                .build()
        )

        superclass(References.resolvedType(declaration))
        addSuperclassConstructorParameter("symbol")
        addSuperclassConstructorParameter("fqName")

        if (declaration is ExportDeclaration.Constructor) {
            kdoc.addStatement("Constructs class %L", declaration.constructedClass.kdoc)
            kdoc.add("\n")
        }

        declaration.buildTypeConstructor(this, kdoc)

        when (declaration) {
            is ExportDeclaration.Class -> declaration.buildClass(this, kdoc)
            is ExportDeclaration.Constructor -> declaration.buildConstructor(this, kdoc)
            is ExportDeclaration.Function -> declaration.buildFunction(this, kdoc)
            is ExportDeclaration.Property -> declaration.buildProperty(this, kdoc)
            is ExportDeclaration.Typealias -> declaration.buildTypealias(this, kdoc)
        }

        addKdoc(kdoc.build())
    }

    //TODO call wrappers, i.e. to extract parameters.  Especially for annotations
    private fun ExportDeclaration.Property.buildProperty(builder: TypeSpec.Builder, kdoc: CodeBlock.Builder) {
        dispatchReceiver?.let {
            kdoc.addStatement("Dispatch receiver: %L", it.type.kdoc)
            kdoc.add("\n")
        }

        if (extensionReceivers.isNotEmpty()) {
            if (extensionReceivers.size > 1) {
                error("Multiple receivers not yet supported")
            }
            val extensionReceiver = extensionReceivers.single()
            kdoc.addStatement("Extension receiver: %L", extensionReceiver.type.kdoc)
            kdoc.add("\n")
        }

        kdoc.addStatement("Type: %L", valueType.kdoc)

        if (hasGetter) {
            builder.addProperty(
                PropertySpec.builder("getter", References.IrSimpleFunctionSymbol)
                    .initializer("owner.getter!!.symbol")
                    .addKdoc("The getter")
                    .build()
            )
            builder.addFunction(buildGetter())
        }

        if (hasSetter) {
            builder.addProperty(
                PropertySpec.builder("setter", References.IrSimpleFunctionSymbol)
                    .initializer("owner.setter!!.symbol")
                    .addKdoc("The setter")
                    .build()
            )
            builder.addFunction(buildSetter())
        }

        if (hasField) {
            builder.addProperty(
                PropertySpec.builder("backingField", References.IrFieldSymbol)
                    .initializer("owner.backingField!!.symbol")
                    .addKdoc("The backing field")
                    .build()
            )
        }

    }

    private fun ExportDeclaration.Property.buildGetter(): FunSpec {
        return buildFunctionCall(
            "get",
            "Call the getter",
            References.IrCall,
            CodeBlock.of("%M(getter)", References.irCall),
            typeParameters,
            dispatchReceiver,
            extensionReceivers,
            listOf(),
            valueType,
            CodeBlock.of("getter.owner.returnType"),
            false
        )
    }

    private fun ExportDeclaration.Property.buildSetter(): FunSpec {
        return buildFunctionCall(
            "set",
            "Call the setter",
            References.IrCall,
            CodeBlock.of("%M(setter)", References.irCall),
            typeParameters,
            dispatchReceiver,
            extensionReceivers,
            listOf(ExportDeclaration.Param("value", 0, false, false, valueType)),
            TypeString("kotlin.Unit"),
            CodeBlock.of("setter.owner.returnType"),
            false
        )
    }

    private fun ExportDeclaration.Typealias.buildTypealias(builder: TypeSpec.Builder, kdoc: CodeBlock.Builder) {
    }

    private fun ExportDeclaration.Class.buildClass(builder: TypeSpec.Builder, kdoc: CodeBlock.Builder) {

    }

    private fun ExportDeclaration.Constructor.buildConstructor(builder: TypeSpec.Builder, kdoc: CodeBlock.Builder) {

        kdoc.addListBlock("Value parameters:", valueParameters) { it.classKdoc() }

        builder.addFunction(buildConstructorCall(false))

        if (valueParameters.any { it.varargs }) {
            builder.addFunction(buildConstructorCall(true))
        }
    }

    private fun ExportDeclaration.Constructor.buildConstructorCall(useVararg: Boolean): FunSpec {
        return buildFunctionCall(
            if (useVararg) "callVararg" else "call",
            "Call the constructor",
            References.IrConstructorCall,
            CodeBlock.of("%M(this, listOf(%L))", References.irCallConstructor, classTypeParams.joinToString(", ") { it.name }),
            classTypeParams,
            null,
            emptyList(),
            valueParameters,
            constructedClass,
            CodeBlock.of("owner.returnType"),
            useVararg
        )

    }

    private fun ExportDeclaration.Function.buildFunction(
        builder: TypeSpec.Builder,
        kdoc: CodeBlock.Builder
    ) {
        dispatchReceiver?.let {
            kdoc.addStatement("Dispatch receiver: %L", it.type.kdoc)
            kdoc.add("\n")
        }

        if (extensionReceivers.isNotEmpty()) {
            if (extensionReceivers.size > 1) {
                error("Multiple receivers not yet supported")
            }
            val extensionReceiver = extensionReceivers.single()
            kdoc.addStatement("Extension receiver: %L", extensionReceiver.type.kdoc)
            kdoc.add("\n")
        }

        kdoc.addListBlock("Value parameters:", valueParameters) { it.classKdoc() }

        kdoc.addStatement("Return type: %L", returnType.kdoc)

        builder.addFunction(buildFunctionCall(false))

        if (valueParameters.any { it.varargs }) {
            builder.addFunction(buildFunctionCall(true))
        }
    }

    private fun ExportDeclaration.Function.buildFunctionCall(useVararg: Boolean): FunSpec {
        return buildFunctionCall(
            if (useVararg) "callVararg" else "call",
            "Call the function",
            References.IrCall,
            CodeBlock.of("%M(this)", References.irCall),
            typeParameters,
            dispatchReceiver,
            extensionReceivers,
            valueParameters,
            returnType,
            CodeBlock.of("owner.returnType"),
            useVararg
        )
    }

    private fun addValueParameters(
        valueParameters: List<ExportDeclaration.Param>,
        useVararg: Boolean,
        call: FunSpec.Builder,
        body: CodeBlock.Builder,
        kdoc: CodeBlock.Builder
    ) {
        valueParameters.forEach {
            val (value, baseType) = if (it.varargs && useVararg) {
                CodeBlock.of(
                    "builder.%M(owner.valueParameters[%L].varargElementType!!.%M(%M), %L.toList())",
                    References.irVararg,
                    it.index,
                    References.substituteTypes,
                    References.irCallTypeSubstitutionMap,
                    it.name
                ) to Iterable::class.asTypeName().parameterizedBy(References.IrExpression)
            } else {
                CodeBlock.of("%L", it.name) to References.IrExpression
            }
            if (it.optional) {
                call.addParameter(ParameterSpec.builder(it.name, baseType.copy(nullable = true)).defaultValue("null").build())
                body.beginControlFlow("if(%L != null)", it.name)
                body.addStatement("putValueArgument(%L, %L)", it.index, value)
                body.endControlFlow()
            } else {
                call.addParameter(it.name, baseType)
                body.addStatement("putValueArgument(%L, %L)", it.index, value)
            }
            kdoc.addStatement("@param %L %L", it.name, it.callKdoc())
        }
    }

    //TODO builder/lambda for lambda arguments
    private fun buildFunctionCall(
        name: String,
        header: String,
        returnType: TypeName,
        callFunction: CodeBlock,
        typeParameters: List<ExportDeclaration.TypeParameter>,
        dispatchReceiver: ExportDeclaration.Receiver?,
        extensionReceivers: List<ExportDeclaration.Receiver>,
        valueParameters: List<ExportDeclaration.Param>,
        irReturnTypeString: TypeString,
        irReturnType: CodeBlock,
        useVararg: Boolean
    ): FunSpec {
        val builder = FunSpec.builder(name)
        builder.returns(returnType)

        builder.addParameter("builder", References.IrBuilderWithScope)
        val body = CodeBlock.builder().beginControlFlow("builder.%L.apply{", callFunction)
        val kdoc = CodeBlock.builder().addStatement(header).add("\n")

        typeParameters.forEach {
            builder.addParameter(it.name, References.IrType)
            body.addStatement("putTypeArgument(%L, %L)", it.index, it.name)
            kdoc.addStatement("@param %L %L", it.name, it.callKdoc())
        }

        dispatchReceiver?.let {
            builder.addParameter("dispatchReceiver", References.IrExpression)
            body.addStatement("this.dispatchReceiver = dispatchReceiver")
            kdoc.addStatement("@param %L %L", "dispatchReceiver", it.type.kdoc)
        }

        if (extensionReceivers.isNotEmpty()) {
            if (extensionReceivers.size > 1) {
                error("Multiple receivers not yet supported")
            }
            val extensionReceiver = extensionReceivers.single()
            builder.addParameter("extensionReceiver", References.IrExpression)
            body.addStatement("this.extensionReceiver = extensionReceiver")
            kdoc.addStatement("@param %L %L", "extensionReceiver", extensionReceiver.type.kdoc)
        }

        if (typeParameters.isNotEmpty()) {
            body.addStatement("type = %L.%M(%M)", irReturnType, References.substituteTypes, References.irCallTypeSubstitutionMap)
        } else {
            body.addStatement("type = %L", irReturnType)
        }

        addValueParameters(valueParameters, useVararg, builder, body, kdoc)

        kdoc.addStatement("@return %L", irReturnTypeString.kdoc)
        builder.addKdoc(kdoc.build())
        builder.addStatement("return %L", body.endControlFlow().build())
        return builder.build()
    }
}