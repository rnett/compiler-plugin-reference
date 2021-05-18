package com.rnett.plugin.generator

import com.rnett.plugin.ExportDeclaration
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.TypeSpec
import java.io.File

object PluginImportGenerator {
    fun generate(baseDirectory: File, declarations: Iterable<ExportDeclaration>, packageName: String, className: String = "Names") {
        FileSpec.builder(packageName, className).apply {
            addComment("GENERATED, DO NOT EDIT")
            indent("    ")
            val referenceClass = TypeSpec.objectBuilder(className)
            val resolvedClassName = "Resolved$className"
            val resolvedClass = TypeSpec.classBuilder(resolvedClassName)

            val tree = DeclarationTree(declarations)
            ReferenceBuilder.buildNames(referenceClass, tree)
            ResolvedBuilder.buildResolved(resolvedClass, packageName, resolvedClassName, className, tree)

            addType(referenceClass.build())
            addType(resolvedClass.build())
        }.build().writeTo(baseDirectory)
    }
}

