package com.rnett.plugin.generator

import com.rnett.plugin.ConstantValue
import com.rnett.plugin.ExportDeclaration
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy

internal object ReferenceBuilder {

    private const val objectName = "Reference"

    fun referenceObject(declarationTree: DeclarationTree, resolvedType: ClassName, nameLookup: FqNameLookup): TypeSpec {
        val builder = TypeSpec.companionObjectBuilder(objectName)

        declarationTree.declaration?.let { builder.setupDeclaration(it, resolvedType) }

        if (declarationTree is DeclarationTree.Package)
            builder.setupPackage(resolvedType)

        declarationTree.children.forEach {
            val nameClass = resolvedType.nestedClass(it.displayName).nestedClass(objectName)
            builder.addProperty(
                PropertySpec.builder(it.displayName, nameClass)
                    .initializer("%L.%L.%L", resolvedType.simpleNames.last(), it.displayName, objectName)
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

        if (declaration is ExportDeclaration.Class && declaration.annotationProperties != null) {
            declaration.buildAnnotationRef(builder, resolvedType, nameLookup)
        }

        return builder.build()
    }

    private fun ExportDeclaration.Class.buildAnnotationRef(
        builder: TypeSpec.Builder,
        resolvedType: ClassName,
        nameLookup: FqNameLookup
    ) {
        val instanceType = resolvedType.nestedClass("Instance")
        if (repeatableAnnotation) {
            builder.addSuperinterface(References.RepeatableAnnotationReference.parameterizedBy(instanceType))
        } else {
            builder.addSuperinterface(References.SingleAnnotationReference.parameterizedBy(instanceType))
        }
        val createFromCtorCall = FunSpec.builder("invoke")
            .addModifiers(KModifier.OPERATOR, KModifier.OVERRIDE)
            .returns(instanceType)
            .addParameter("annotation", References.IrConstructorCall)
            .addParameter("context", References.IrPluginContext)

        createFromCtorCall.addStatement(
            "val ctorSig = annotation.symbol.owner.%M.symbol.signature",
            References.parentAsClass
        )
        createFromCtorCall.beginControlFlow("require(ctorSig == signature)")
        createFromCtorCall.addStatement(
            "%P",
            "Constructor call has different IdSignature than exported for \"\$fqName\": \$ctorSig"
        )
        createFromCtorCall.endControlFlow()

        createFromCtorCall.addStatement(
            "return %T(%L)",
            instanceType,
            CodeBlock.builder().apply {
                annotationProperties!!.entries.forEachIndexed { i, it ->
                    add(
                        "%L = %L${if (i != annotationProperties!!.size - 1) "," else ""}\n",
                        it.key,
                        it.value.value("context", "annotation", nameLookup::getClassNameForFqName)
                    )
                }
            }.build()
        )
        builder.addFunction(createFromCtorCall.build())

        val createInstance = FunSpec.builder("invoke")
            .addModifiers(KModifier.OPERATOR)
        createInstance.addCode("return Instance(")
        annotationProperties!!.entries.forEachIndexed { i, it ->
            createInstance.addParameter(it.key, it.value.kind.valueType(nameLookup::getClassNameForFqName))
            createInstance.addCode("${it.key} = ${it.key}" + if (i != annotationProperties!!.size - 1) ", " else "")
        }
        createInstance.addCode(")")

        builder.addFunction(createInstance.build())
        //TODO move annotation Instance secondary constructor here, make check IrConstructorCall IdSignature
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

        builder.addFunction(
            FunSpec.builder("toString")
                .addModifiers(KModifier.OVERRIDE)
                .returns(STRING)
                .addStatement("return %P", "${resolvedType.simpleName}.\$name")
                .build()
        )

        return builder.build()
    }
}