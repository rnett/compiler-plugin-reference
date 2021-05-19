package com.rnett.plugin.generator

import com.rnett.plugin.ExportDeclaration
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec


private fun CodeBlock.Builder.addTypeParamKdoc(typeParameters: List<ExportDeclaration.TypeParameter>) =
    addListBlock("Type parameters:", typeParameters) { it.classKdoc() }

fun ExportDeclaration.buildTypeConstructor(builder: TypeSpec.Builder, kdoc: CodeBlock.Builder) {
    when (this) {
        is ExportDeclaration.Class -> {
            kdoc.addTypeParamKdoc(typeParameters)
            TypeConstructorBuilder.ClassTypeWith.build(builder, this, typeParameters)
            if (typeParameters.isNotEmpty())
                builder.addFunction(TypeConstructorBuilder.ClassTypeArgument.buildFunction(typeParameters))
        }
        is ExportDeclaration.Constructor -> {
            kdoc.addListBlock("Class type parameters:", classTypeParams) { it.classKdoc() }
            TypeConstructorBuilder.Constructor.build(builder, this, classTypeParams)
        }
        is ExportDeclaration.Function -> {
            kdoc.addTypeParamKdoc(typeParameters)
            TypeConstructorBuilder.Function.build(builder, this, typeParameters)
        }
        is ExportDeclaration.Property -> {
            kdoc.addTypeParamKdoc(typeParameters)
            TypeConstructorBuilder.Property.build(builder, this, typeParameters)
        }
        is ExportDeclaration.Typealias -> {
            kdoc.addTypeParamKdoc(typeParameters)
            TypeConstructorBuilder.Typealias.build(builder, this, typeParameters)
        }
    }
}

sealed class TypeConstructorBuilder(val name: String, val header: String, val returnType: TypeName, val paramType: TypeName) {
    open fun modifyParameter(param: ExportDeclaration.TypeParameter, builder: ParameterSpec.Builder) {

    }

    open fun buildParameter(param: ExportDeclaration.TypeParameter): ParameterSpec {
        val builder = ParameterSpec.builder(param.name, paramType)
        modifyParameter(param, builder)
        builder.addKdoc("%L", param.callKdoc())
        return builder.build()
    }

    abstract fun returnValue(typeParameters: List<ExportDeclaration.TypeParameter>, paramList: String): CodeBlock

    abstract val propertyValue: CodeBlock

    abstract fun validate(declaration: ExportDeclaration)

    fun build(builder: TypeSpec.Builder, declaration: ExportDeclaration, typeParameters: List<ExportDeclaration.TypeParameter>) {
        validate(declaration)
        if (typeParameters.isEmpty())
            builder.addProperty(buildProperty())
        else
            builder.addFunction(buildFunction(typeParameters))
    }

    fun buildFunction(typeParameters: List<ExportDeclaration.TypeParameter>): FunSpec {
        val builder = FunSpec.builder(name)
            .returns(returnType)
        val kdoc = CodeBlock.builder().addStatement(header)
        builder.addKdoc(kdoc.build())

        typeParameters.forEach {
            builder.addParameter(buildParameter(it))
        }

        builder.addStatement("return %L", returnValue(typeParameters, typeParameters.joinToString(", ") { it.name }))
        return builder.build()
    }

    private fun buildProperty(): PropertySpec {
        val builder = PropertySpec.builder(name, returnType)
        val kdoc = CodeBlock.builder().addStatement(header)
        builder.addKdoc(kdoc.build())
        builder.initializer(propertyValue)
        return builder.build()
    }

    object ClassTypeArgument : TypeConstructorBuilder("type", "Get the class's type.", References.IrSimpleType, References.IrTypeArgument) {
        override fun modifyParameter(param: ExportDeclaration.TypeParameter, builder: ParameterSpec.Builder) {
            builder.defaultValue("%T", References.IrStarProjectionImpl)
        }

        override fun returnValue(typeParameters: List<ExportDeclaration.TypeParameter>, paramList: String): CodeBlock = CodeBlock.of(
            "%M(listOf(%L));",
            References.typeWithArguments,
            paramList
        )

        override val propertyValue: CodeBlock = CodeBlock.of("owner.%M()", References.typeWith)

        override fun validate(declaration: ExportDeclaration) {
            check(declaration is ExportDeclaration.Class) { "Wrong declaration type, Class is required" }
        }
    }

    object ClassTypeWith : TypeConstructorBuilder("type", "Get the class's type.", References.IrSimpleType, References.IrType) {
        override fun returnValue(typeParameters: List<ExportDeclaration.TypeParameter>, paramList: String): CodeBlock = CodeBlock.of(
            "owner.%M(%L)",
            References.typeWith,
            paramList
        )

        override val propertyValue: CodeBlock = CodeBlock.of("owner.%M()", References.typeWith)

        override fun validate(declaration: ExportDeclaration) {
            check(declaration is ExportDeclaration.Class) { "Wrong declaration type, Class is required" }
        }
    }

    object Typealias : TypeConstructorBuilder("type", "Get the expanded type.", References.IrType, References.IrType) {
        override fun returnValue(typeParameters: List<ExportDeclaration.TypeParameter>, paramList: String): CodeBlock = CodeBlock.of(
            "%L.%M(owner.typeParameters, listOf(%L))",
            propertyValue,
            References.substituteTypes,
            paramList
        )

        override val propertyValue: CodeBlock = CodeBlock.of("owner.expandedType")

        override fun validate(declaration: ExportDeclaration) {
            check(declaration is ExportDeclaration.Typealias) { "Wrong declaration type, Typealias is required" }
        }
    }

    object Constructor : TypeConstructorBuilder("constructedType", "Get the constructed type.", References.IrType, References.IrType) {
        override fun returnValue(typeParameters: List<ExportDeclaration.TypeParameter>, paramList: String): CodeBlock = CodeBlock.of(
            "%L.%M(owner.%M.typeParameters, listOf(%L))",
            propertyValue,
            References.substituteTypes,
            References.constructedClass,
            paramList
        )

        override val propertyValue: CodeBlock = CodeBlock.of("owner.%M", References.constructedClassType)

        override fun validate(declaration: ExportDeclaration) {
            check(declaration is ExportDeclaration.Constructor) { "Wrong declaration type, Constructor is required" }
        }
    }

    object Function : TypeConstructorBuilder("returnType", "Get the return type.", References.IrType, References.IrType) {
        override fun returnValue(typeParameters: List<ExportDeclaration.TypeParameter>, paramList: String): CodeBlock = CodeBlock.of(
            "%L.%M(owner.typeParameters, listOf(%L))",
            propertyValue,
            References.substituteTypes,
            paramList
        )

        override val propertyValue: CodeBlock = CodeBlock.of("owner.returnType")

        override fun validate(declaration: ExportDeclaration) {
            check(declaration is ExportDeclaration.Function) { "Wrong declaration type, Function is required" }
        }
    }

    object Property : TypeConstructorBuilder("type", "Get the property's type.", References.IrType, References.IrType) {

        override fun returnValue(typeParameters: List<ExportDeclaration.TypeParameter>, paramList: String): CodeBlock = CodeBlock.of(
            "%L.%M(owner.getter!!.typeParameters, listOf(%L))",
            propertyValue,
            References.substituteTypes,
            paramList
        )

        override val propertyValue: CodeBlock = CodeBlock.of("(owner.getter?.returnType ?: owner.backingField?.type)!!")

        override fun validate(declaration: ExportDeclaration) {
            check(declaration is ExportDeclaration.Property) { "Wrong declaration type, Property is required" }
            if (declaration.typeParameters.isNotEmpty() && !declaration.hasGetter) {
                error("Property with type parameters must have a getter.  How did you make this, anyways?")
            }
        }
    }
}