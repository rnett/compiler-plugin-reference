package com.rnett.plugin.generator

import com.rnett.plugin.ExportDeclaration
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy

typealias TypeAndFuncs = Pair<TypeSpec, List<FunSpec>>?

//TODO I would like to be able to get the Resolved classes from an instance.  Not possible b/c it requires context.
//  But can I get rid of context property & use children filtering to find children
object InstanceBuilder {

    const val name: String = "Instance"
    fun instanceBuilder() = TypeSpec.classBuilder(name)

    internal fun buildInstance(
        className: ClassName,
        declaration: ExportDeclaration,
        nameLookup: FqNameLookup
    ): Pair<List<TypeSpec>, List<FunSpec>> {
        val (type, functions) = when (declaration) {
            is ExportDeclaration.Class -> declaration.buildClassInstance(className, nameLookup)
            is ExportDeclaration.Constructor -> declaration.buildConstructorInstance(className)
            is ExportDeclaration.Function -> declaration.buildFunctionInstance(className)
            is ExportDeclaration.Property -> declaration.buildPropertyInstance(className)
            is ExportDeclaration.Typealias -> declaration.buildTypealiasInstance(className)
        }?.let { it.first to it.second.toMutableList() } ?: return emptyList<TypeSpec>() to emptyList<FunSpec>()

        if (declaration is ExportDeclaration.Property) {
            functions += declaration.addAccessorInstanceGetters(className)
        }

        if (declaration is ExportDeclaration.Property) {
            return (listOf(type) + declaration.additionalPropertyDeclarations(className.nestedClass(name))) to functions
        } else
            return listOf(type) to functions
    }

    private fun TypeSpec.Builder.addSignatureCheck(signature: CodeBlock, className: ClassName) {
        addInitializerBlock(
            CodeBlock.builder()
                .addStatement("val signature = %L", signature)
                .addStatement("val requiredSignature = %T.signature", className)
                .beginControlFlow("require(signature == requiredSignature)")
                .addStatement(
                    "%P",
                    "Instance's signature \$signature did not match the required signature of \$requiredSignature"
                )
                .endControlFlow()
                .build()
        )
    }

    fun ExportDeclaration.Class.buildEnumEntries(className: ClassName): List<TypeSpec> {
        return enumNames!!.map { (name, _) ->
            TypeSpec.classBuilder(name).apply {
                primaryConstructor(
                    FunSpec.constructorBuilder()
                        .addParameter("symbol", References.IrEnumEntrySymbol)
                        .build()
                )
                superclass(className.nestedClass("Instance"))
                addSuperclassConstructorParameter("%T", className.nestedClass("Entries").nestedClass(name))
                addSuperclassConstructorParameter("symbol")
            }.build()
        }
    }

    @OptIn(ExperimentalStdlibApi::class)
    private fun ExportDeclaration.Class.buildClassInstance(
        className: ClassName,
        nameLookup: FqNameLookup
    ): TypeAndFuncs {
        enumNames?.let { enumNames ->
            val instanceType = className.nestedClass("Instance")
            val instanceBuilder = TypeSpec.classBuilder("Instance").apply {
                addModifiers(KModifier.SEALED)
                superclass(References.ResolvedEnumEntry.parameterizedBy(instanceType))

                primaryConstructor(
                    FunSpec.constructorBuilder()
                        .addParameter("entry", className.nestedClass("Entries"))
                        .addParameter("symbol", References.IrEnumEntrySymbol)
                        .build()
                )

                addSuperclassConstructorParameter("entry, symbol")

                addInitializerBlock(
                    CodeBlock.builder()
                        .beginControlFlow("check(entry.signature == symbol.signature)")
                        .addStatement("%P", "Symbol's signature does not match entry ${'$'}entry")
                        .endControlFlow()
                        .build()
                )
            }

            val methods = buildList<FunSpec> {
                add(
                    FunSpec.builder("entry").apply {
                        addParameter("symbol", References.IrEnumEntrySymbol)
                        returns(className.nestedClass("Entries"))
                        beginControlFlow(
                            "require(symbol.owner.%M.%M(fqName))",
                            References.parentAsClass,
                            References.hasEqualFqName
                        )
                        addStatement("%P", "Enum symbol \$symbol is not an entry of \$fqName")
                        endControlFlow()
                        addStatement("return Entries.valueOf(symbol.owner.name.asString())")
                    }.build()
                )
                add(
                    FunSpec.builder("entryOrNull").apply {
                        addParameter("symbol", References.IrEnumEntrySymbol)
                        returns(className.nestedClass("Entries").copy(nullable = true))
                        beginControlFlow(
                            "if( !(symbol.owner.%M.%M(fqName)))",
                            References.parentAsClass,
                            References.hasEqualFqName
                        )
                        addStatement("return null")
                        endControlFlow()
                        beginControlFlow("return Entries.values().firstOrNull")
                        addStatement("it.name == symbol.owner.name.asString()")
                        endControlFlow()
                    }.build()
                )

                add(
                    FunSpec.builder("instance").apply {
                        addParameter("symbol", References.IrEnumEntrySymbol)
                        returns(instanceType)
                        addStatement("return entry(symbol).instance(symbol)")
                    }.build()
                )

                add(
                    FunSpec.builder("instanceOrNull").apply {
                        addParameter("symbol", References.IrEnumEntrySymbol)
                        returns(instanceType.copy(nullable = true))
                        addStatement("return entryOrNull(symbol)?.instance(symbol)")
                    }.build()
                )

                add(
                    FunSpec.builder("instance").apply {
                        addParameter("get", References.IrGetEnumValue)
                        returns(instanceType)
                        addStatement("return instance(get.symbol)")
                    }.build()
                )

                add(
                    FunSpec.builder("instanceOrNull").apply {
                        addParameter("get", References.IrGetEnumValue)
                        returns(instanceType.copy(nullable = true))
                        addStatement("return instanceOrNull(get.symbol)")
                    }.build()
                )
            }


            return instanceBuilder.build() to methods
        }

        if (this.isAnnotation) {
            val instanceType = className.nestedClass("Instance")
            val builder = TypeSpec.classBuilder("Instance")
                .addModifiers(KModifier.DATA)

            val ctor = FunSpec.constructorBuilder()
            val secondary = FunSpec.constructorBuilder()
                .addParameter("annotation", References.IrConstructorCall)
                .addParameter("context", References.IrPluginContext)

            annotationProperties!!.forEach {
                //TODO pass platform
                val type = it.value.kind.valueType(nameLookup::getClassNameForFqName)
                ctor.addParameter(it.key, type)
                builder.addProperty(
                    PropertySpec.builder(it.key, type)
                        .initializer(it.key)
                        .build()
                )
            }

            secondary.callThisConstructor(annotationProperties!!.map {
                CodeBlock.of(
                    "%L = %L\n",
                    it.key,
                    it.value.value("context", "annotation", nameLookup::getClassNameForFqName)
                )
            })

            builder.primaryConstructor(ctor.build())
            builder.addFunction(secondary.build())

            return builder.build() to emptyList()
        }

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

    private fun ExportDeclaration.Constructor.buildConstructorInstance(className: ClassName): TypeAndFuncs {
        val builder = instanceBuilder()

        builder.primaryConstructor(
            FunSpec.constructorBuilder().addParameter("call", References.IrConstructorCall).build()
        )
        builder.addProperty(PropertySpec.builder("call", References.IrConstructorCall).initializer("call").build())
        val getSignature = CodeBlock.of("call.symbol.signature")

        builder.addSignatureCheck(getSignature, className)

        builder.addTypeParameters(classTypeParams)
        builder.addValueParameters(valueParameters)

        return builder.build() to builderMethods(
            "instance",
            "call",
            References.IrConstructorCall,
            className,
            getSignature
        )
    }

    private fun ExportDeclaration.Function.buildFunctionInstance(className: ClassName): TypeAndFuncs {
        val builder = instanceBuilder()

        builder.primaryConstructor(FunSpec.constructorBuilder().addParameter("call", References.IrCall).build())
        builder.addProperty(PropertySpec.builder("call", References.IrCall).initializer("call").build())
        val getSignature = CodeBlock.of("call.symbol.signature")

        builder.addSignatureCheck(getSignature, className)

        builder.addTypeParameters(typeParameters)
        builder.addValueParameters(valueParameters)
        builder.addReceivers(dispatchReceiver, extensionReceivers)

        return builder.build() to builderMethods("instance", "call", References.IrCall, className, getSignature)
    }

    private fun ExportDeclaration.Property.buildPropertyInstance(className: ClassName): TypeAndFuncs {
        val builder = instanceBuilder()

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

        return builder.build() to builderMethods("accessorInstance", "call", References.IrCall, className, getSignature)
    }

    private fun ExportDeclaration.Property.additionalPropertyDeclarations(instanceClass: ClassName): List<TypeSpec> {

        fun TypeSpec.Builder.common(isRightAccessor: MemberName, rightAccessorName: String) {
            superclass(instanceClass)
            primaryConstructor(FunSpec.constructorBuilder().addParameter("call", References.IrCall).build())
            addSuperclassConstructorParameter("call")
            addInitializerBlock(
                CodeBlock.builder()
                    .beginControlFlow("require(call.symbol.owner.%M)", isRightAccessor)
                    .addStatement(
                        "%P",
                        "Instance \${call.symbol} is not the right type of accessor, expected a $rightAccessorName"
                    )
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

    private fun ExportDeclaration.Typealias.buildTypealiasInstance(className: ClassName): TypeAndFuncs {
        return null
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

    private fun TypeSpec.Builder.addReceivers(
        dispatchReceiver: ExportDeclaration.Receiver?,
        extensionReceivers: List<ExportDeclaration.Receiver>
    ) {
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
