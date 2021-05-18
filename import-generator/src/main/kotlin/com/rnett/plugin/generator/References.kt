package com.rnett.plugin.generator

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.STRING

object References {
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
    val IrBuilderWithScope = ClassName.bestGuess("org.jetbrains.kotlin.ir.builders.IrBuilderWithScope")

    val IrType = ClassName.bestGuess("org.jetbrains.kotlin.ir.types.IrType")
    val IrSimpleType = ClassName.bestGuess("org.jetbrains.kotlin.ir.types.IrSimpleType")
    val IrTypeArgument = ClassName.bestGuess("org.jetbrains.kotlin.ir.types.IrTypeArgument")
    val IrExpression = ClassName.bestGuess("org.jetbrains.kotlin.ir.expressions.IrExpression")
    val IrCall = ClassName.bestGuess("org.jetbrains.kotlin.ir.expressions.IrCall")
    val IrConstructorCall = ClassName.bestGuess("org.jetbrains.kotlin.ir.expressions.IrConstructorCall")

    val IrStarProjectionImpl = ClassName.bestGuess("org.jetbrains.kotlin.ir.types.impl.IrStarProjectionImpl")

    val irCall = MemberName("org.jetbrains.kotlin.ir.builders", "irCall")
    val irCallConstructor = MemberName("org.jetbrains.kotlin.ir.builders", "irCallConstructor")
    val irVararg = MemberName("org.jetbrains.kotlin.ir.builders", "irVararg")
    val irCallTypeSubstitutionMap = MemberName("org.jetbrains.kotlin.ir.util", "typeSubstitutionMap")
    val substituteTypes = MemberName("org.jetbrains.kotlin.ir.util", "substitute")
    val typeWith = MemberName("org.jetbrains.kotlin.ir.types", "typeWith")
    val typeWithArguments = MemberName("org.jetbrains.kotlin.ir.types", "typeWithArguments")
}