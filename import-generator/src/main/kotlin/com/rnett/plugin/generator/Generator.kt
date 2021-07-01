package com.rnett.plugin.generator

import com.rnett.plugin.ExportDeclaration
import com.rnett.plugin.Platform
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import java.io.File

const val contextPropName = "_context"
private val contextConstructorParameter = ParameterSpec.builder(contextPropName, References.IrPluginContext).build()
fun FunSpec.Builder.addContextParameter() = addParameter(contextConstructorParameter)

fun TypeSpec.Builder.addContextConstructor(): TypeSpec.Builder = apply {
    primaryConstructor(FunSpec.constructorBuilder().addContextParameter().build())
}

fun TypeSpec.Builder.addContextProperty(): TypeSpec.Builder = apply {
    addProperty(
        PropertySpec.builder(contextPropName, References.IrPluginContext)
            .initializer("%N", contextPropName)
            .addModifiers(KModifier.PRIVATE)
            .build()
    )
}

//TODO support for renames.  Nothing is done with them ATM.
object PluginImportGenerator {
    fun generateMultiplatform(
        baseDirectory: File,
        declarations: Map<Platform, List<ExportDeclaration>>,
        packageName: String,
        fileName: String,
        className: String = "Names"
    ) {
        //TODO new, source set based commonizer
        generate(
            baseDirectory,
            commonize(declarations.mapValues { DeclarationTree(it.value) }),
            packageName,
            fileName,
            className
        )
    }

    fun generateSingle(
        baseDirectory: File,
        platform: Platform,
        declarations: List<ExportDeclaration>,
        packageName: String,
        fileName: String,
        className: String = "Names"
    ) {
        //TODO platform checks
        generate(baseDirectory, DeclarationTree(declarations), packageName, fileName, className)
    }

    fun generate(
        baseDirectory: File,
        declarationTree: DeclarationTree,
        packageName: String,
        fileName: String,
        className: String
    ) {
        FileSpec.builder(packageName, fileName).apply {
            addComment("GENERATED, DO NOT EDIT")
            indent("    ")

            val rootClass = ClassName(packageName, className)
            val lookup = FqNameLookup(declarationTree, rootClass)
            addType(generate(rootClass, declarationTree, lookup))

        }.build().writeTo(baseDirectory)
    }

    private fun generate(fqName: ClassName, declarationTree: DeclarationTree, lookup: FqNameLookup): TypeSpec {
        val builder = TypeSpec.classBuilder(fqName.simpleNames.last())

        val namesBuilder = ReferenceBuilder.referenceObject(declarationTree, fqName).toBuilder()
        ResolvedBuilder.build(builder, namesBuilder, fqName, lookup, declarationTree)

        declarationTree.children.forEach {
            builder.addType(generate(fqName.nestedClass(it.displayName), it, lookup))
        }

        builder.addType(namesBuilder.build())
        return builder.build()
    }

}

