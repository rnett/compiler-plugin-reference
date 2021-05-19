package com.rnett.plugin.generator

import com.rnett.plugin.ExportDeclaration
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

object PluginImportGenerator {
    fun generate(baseDirectory: File, declarations: Iterable<ExportDeclaration>, packageName: String, className: String = "Names") {

        FileSpec.builder(packageName, className).apply {
            addComment("GENERATED, DO NOT EDIT")
            indent("    ")

            val tree = DeclarationTree(declarations)
            addType(generate(ClassName(packageName, className), tree))

        }.build().writeTo(baseDirectory)
    }

    fun generate(fqName: ClassName, declarationTree: DeclarationTree): TypeSpec {
        val builder = TypeSpec.classBuilder(fqName.simpleNames.last())

        ResolvedBuilder.build(builder, fqName, declarationTree)

        declarationTree.children.forEach {
            builder.addType(generate(fqName.nestedClass(it.displayName), it))
        }

        builder.addType(ReferenceBuilder.referenceObject(declarationTree, fqName))
        return builder.build()
    }

}

