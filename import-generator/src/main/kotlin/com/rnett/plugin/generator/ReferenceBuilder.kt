package com.rnett.plugin.generator

import com.rnett.plugin.ConstantValue
import com.rnett.plugin.ExportDeclaration
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy

internal object ReferenceBuilder {

    private const val objectName = "Reference"

    fun referenceObject(declarationTree: DeclarationTree, fqName: ClassName): TypeSpec {
        val builder = TypeSpec.companionObjectBuilder(objectName)

        declarationTree.declaration?.let { builder.setupDeclaration(it, fqName) }

        if (declarationTree is DeclarationTree.Package)
            builder.setupPackage(fqName)

        declarationTree.children.forEach {
            val nameClass = fqName.nestedClass(it.displayName).nestedClass(objectName)
            builder.addProperty(
                PropertySpec.builder(it.displayName, nameClass)
                    .initializer("%L.%L.%L", fqName.simpleNames.last(), it.displayName, objectName)
                    .build()
            )
        }

        val declaration = declarationTree.declaration

        if (declaration is ExportDeclaration.Property && declaration.constantValue != null) {
            val value = declaration.constantValue!!
            val type = when (value) {
                is ConstantValue.Boolean -> BOOLEAN
                is ConstantValue.Byte -> BYTE
                is ConstantValue.Char -> CHAR
                is ConstantValue.Double -> DOUBLE
                is ConstantValue.Float -> FLOAT
                is ConstantValue.Int -> INT
                is ConstantValue.Long -> LONG
                ConstantValue.Null -> NOTHING.copy(nullable = true)
                is ConstantValue.Short -> SHORT
                is ConstantValue.String -> STRING
            }
            builder.addProperty(
                PropertySpec.builder("value", type, KModifier.CONST)
                    .initializer(value.valueAsString())
                    .build()
            )
        }

        return builder.build()
    }

    private fun TypeSpec.Builder.setupPackage(resolvedType: ClassName): TypeSpec.Builder = apply {
        addFunction(
            FunSpec.builder("invoke")
                .addParameter("context", References.IrPluginContext)
                .addStatement("return %T(context)", resolvedType)
                .addModifiers(KModifier.OPERATOR)
                .build()
        )
    }

    private fun TypeSpec.Builder.setupDeclaration(
        declaration: ExportDeclaration,
        resolvedType: ClassName
    ): TypeSpec.Builder = apply {
        superclass(References.referenceType(declaration).parameterizedBy(resolvedType))

        addSuperclassConstructorParameter(declaration.referenceFqName.toFqName())
        addSuperclassConstructorParameter(declaration.signature.toIdSignature())

        addFunction(
            FunSpec.builder("getResolvedReference")
                .returns(resolvedType)
                .addModifiers(KModifier.OVERRIDE)
                .addParameter("context", References.IrPluginContext)
                .addParameter("symbol", References.symbolType(declaration))
                .addStatement("return %T(context, symbol)", resolvedType)
                .build()
        )
    }

    fun ExportDeclaration.Class.buildEnumEntryType(resolvedType: ClassName): TypeSpec {
        require(enumNames != null)
        val builder = TypeSpec.enumBuilder("Entries")
        builder.addSuperinterface(
            References.EnumEntryReference.parameterizedBy(
                resolvedType.nestedClass("Instance"),
                resolvedType
            )
        )

        builder.addProperty(
            PropertySpec.builder("classReference", resolvedType.nestedClass("Reference"), KModifier.OVERRIDE)
                .initializer("%T", resolvedType.nestedClass("Reference"))
                .build()
        )

        builder.primaryConstructor(
            FunSpec.constructorBuilder().addParameter("signature", References.IdSignaturePublicSignature).build()
        )

        builder.addProperty(
            PropertySpec.builder("signature", References.IdSignaturePublicSignature, KModifier.OVERRIDE)
                .initializer("signature")
                .build()
        )

        enumNames!!.forEach { (name, sig) ->
            builder.addEnumConstant(
                name,
                TypeSpec.anonymousClassBuilder().addSuperclassConstructorParameter(sig.toIdSignature()).build()
            )
        }

        builder.addFunction(FunSpec.builder("instance")
            .returns(resolvedType.nestedClass("Instance"))
            .addParameter("symbol", References.IrEnumEntrySymbol)
            .addCode(CodeBlock.builder()
                .beginControlFlow("return when(this) {")
                .apply {
                    enumNames!!.forEach { (name, _) ->
                        addStatement("%L -> %L(symbol)", name, name)
                    }
                }
                .endControlFlow()
                .build())
            .build())

        builder.addFunction(
            FunSpec.builder("getResolvedReference")
                .addModifiers(KModifier.OVERRIDE)
                .returns(resolvedType.nestedClass("Instance"))
                .addParameter("context", References.IrPluginContext)
                .addParameter("symbol", References.IrEnumEntrySymbol)
                .addCode("return instance(symbol)")
                .build()
        )

        return builder.build()
    }
}