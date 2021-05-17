package com.rnett.plugin.generator

import com.rnett.plugin.ResolvedName
import com.rnett.plugin.Signature
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.STRING

object Types {
    val ClassReference = ClassName.bestGuess("com.rnett.plugin.ClassReference")
    val TypealiasReference = ClassName.bestGuess("com.rnett.plugin.TypealiasReference")
    val ConstructorReference = ClassName.bestGuess("com.rnett.plugin.ConstructorReference")
    val FunctionReference = ClassName.bestGuess("com.rnett.plugin.FunctionReference")
    val PropertyReference = ClassName.bestGuess("com.rnett.plugin.PropertyReference")

    val ResolvedClass = ClassName.bestGuess("com.rnett.plugin.ResolvedClass")
    val ResolvedTypealias = ClassName.bestGuess("com.rnett.plugin.ResolvedTypealias")
    val ResolvedConstructor = ClassName.bestGuess("com.rnett.plugin.ResolvedConstructor")
    val ResolvedFunction = ClassName.bestGuess("com.rnett.plugin.ResolvedFunction")
    val ResolvedProperty = ClassName.bestGuess("com.rnett.plugin.ResolvedProperty")

    val String = STRING

    val FqName = ClassName.bestGuess("org.jetbrains.kotlin.name.FqName")
    val IdSignaturePublicSignature = ClassName.bestGuess("org.jetbrains.kotlin.ir.util.IdSignature.PublicSignature")
    val IrPluginContext = ClassName.bestGuess("org.jetbrains.kotlin.backend.common.extensions.IrPluginContext")
}

internal fun ResolvedName.toFqName(): CodeBlock = CodeBlock.of("%T(%S)", Types.FqName, this.fqName)

internal fun Signature.toIdSignature(): CodeBlock =
    CodeBlock.of("%T(%S, %S, %L, %L)", Types.IdSignaturePublicSignature, this.packageFqName, this.declarationFqName, this.id, this.mask)