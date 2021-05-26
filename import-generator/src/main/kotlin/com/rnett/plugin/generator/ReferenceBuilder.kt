package com.rnett.plugin.generator

import com.rnett.plugin.ConstantValue
import com.rnett.plugin.ExportDeclaration
import com.squareup.kotlinpoet.BOOLEAN
import com.squareup.kotlinpoet.BYTE
import com.squareup.kotlinpoet.CHAR
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.DOUBLE
import com.squareup.kotlinpoet.FLOAT
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.INT
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.LONG
import com.squareup.kotlinpoet.NOTHING
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.SHORT
import com.squareup.kotlinpoet.STRING
import com.squareup.kotlinpoet.TypeSpec

internal object ReferenceBuilder {

    private const val objectName = "Reference"

    //TODO add constant value
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

    private fun TypeSpec.Builder.setupDeclaration(declaration: ExportDeclaration, resolvedType: ClassName): TypeSpec.Builder = apply {
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
}