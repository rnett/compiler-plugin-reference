package com.rnett.plugin.generator

import com.rnett.plugin.ExportDeclaration
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec

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