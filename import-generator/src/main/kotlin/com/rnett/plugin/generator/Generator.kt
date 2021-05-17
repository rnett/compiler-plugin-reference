package com.rnett.plugin.generator

import com.rnett.plugin.ExportDeclaration
import com.rnett.plugin.ResolvedName
import com.rnett.plugin.Signature
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.STRING
import com.squareup.kotlinpoet.TypeSpec
import java.io.File

object PluginImportGenerator {
    fun generate(baseDirectory: File, declarations: Iterable<ExportDeclaration>, packageName: String, className: String = "Names") {
        FileSpec.builder(packageName, className).apply {
            addComment("GENERATED, DO NOT EDIT")
            indent("    ")
            val referenceClass = TypeSpec.objectBuilder(className)
            val resolvedClass = TypeSpec.objectBuilder("Resolved$className")

            val tree = DeclarationTree(declarations)
            ReferenceBuilder.buildNames(referenceClass, tree)

            addType(referenceClass.build())
            addType(resolvedClass.build())
        }.build().writeTo(baseDirectory)
    }
}

