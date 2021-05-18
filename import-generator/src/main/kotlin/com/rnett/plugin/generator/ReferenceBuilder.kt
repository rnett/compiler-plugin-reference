package com.rnett.plugin.generator

import com.rnett.plugin.ExportDeclaration
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec

internal object ReferenceBuilder {
    internal fun buildNames(references: TypeSpec.Builder, declarations: DeclarationTree) {
        addDeclarationTree(references, declarations)
    }

    private fun addDeclarationTree(references: TypeSpec.Builder, declarationTree: DeclarationTree) {
        when (declarationTree) {
            is DeclarationTree.Package -> {
                if (declarationTree.isRoot) {
                    declarationTree.children.forEach { addDeclarationTree(references, it) }
                } else {
                    val newReferences = TypeSpec.objectBuilder(declarationTree.displayName)

                    declarationTree.children.forEach { addDeclarationTree(newReferences, it) }

                    references.addType(newReferences.build())
                }
            }
            is DeclarationTree.Class -> {
                if (declarationTree.children.isEmpty()) {
                    references.addProperty(addNameDeclaration(declarationTree.displayName, declarationTree.declaration))
                } else {
                    val newReferences = TypeSpec.objectBuilder(declarationTree.displayName)
                    val newResolved = TypeSpec.objectBuilder(declarationTree.displayName)

                    newReferences.superclass(References.ClassReference)
                        .addSuperclassConstructorParameter(declarationTree.declaration.fqName.toFqName())
                        .addSuperclassConstructorParameter(declarationTree.declaration.signature.toIdSignature())

                    declarationTree.children.forEach { addDeclarationTree(newReferences, it) }

                    references.addType(newReferences.build())
                }
            }
            is DeclarationTree.Leaf -> {
                references.addProperty(addNameDeclaration(declarationTree.displayName, declarationTree.declaration))
            }
        }
    }

    private fun addNameDeclaration(name: String, declaration: ExportDeclaration): PropertySpec = when (declaration) {
        is ExportDeclaration.Class -> PropertySpec.builder(name, References.ClassReference)
            .initializer("%T(%L, %L)", References.ClassReference, declaration.fqName.toFqName(), declaration.signature.toIdSignature())
            .build()

        is ExportDeclaration.Constructor -> PropertySpec.builder(name, References.ConstructorReference)
            .initializer("%T(%L, %L)", References.ConstructorReference, declaration.classFqName.toFqName(), declaration.signature.toIdSignature())
            .build()

        is ExportDeclaration.Function -> PropertySpec.builder(name, References.FunctionReference)
            .initializer("%T(%L, %L)", References.FunctionReference, declaration.fqName.toFqName(), declaration.signature.toIdSignature())
            .build()

        is ExportDeclaration.Property -> PropertySpec.builder(name, References.PropertyReference)
            .initializer("%T(%L, %L)", References.PropertyReference, declaration.fqName.toFqName(), declaration.signature.toIdSignature())
            .build()

        is ExportDeclaration.Typealias -> PropertySpec.builder(name, References.TypealiasReference)
            .initializer("%T(%L, %L)", References.TypealiasReference, declaration.fqName.toFqName(), declaration.signature.toIdSignature())
            .build()
    }
}