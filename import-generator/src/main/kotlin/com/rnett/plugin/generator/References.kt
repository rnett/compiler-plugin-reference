package com.rnett.plugin.generator

import com.rnett.plugin.ExportDeclaration
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.STRING

object References {
    val ClassReference = ClassName.bestGuess("com.rnett.plugin.ClassReference")
    val TypealiasReference = ClassName.bestGuess("com.rnett.plugin.TypealiasReference")
    val ConstructorReference = ClassName.bestGuess("com.rnett.plugin.ConstructorReference")
    val FunctionReference = ClassName.bestGuess("com.rnett.plugin.FunctionReference")
    val PropertyReference = ClassName.bestGuess("com.rnett.plugin.PropertyReference")
    val EnumEntryReference = ClassName.bestGuess("com.rnett.plugin.EnumEntryReference")

    val ResolvedClass = ClassName.bestGuess("com.rnett.plugin.ResolvedClass")
    val ResolvedTypealias = ClassName.bestGuess("com.rnett.plugin.ResolvedTypealias")
    val ResolvedConstructor = ClassName.bestGuess("com.rnett.plugin.ResolvedConstructor")
    val ResolvedFunction = ClassName.bestGuess("com.rnett.plugin.ResolvedFunction")
    val ResolvedProperty = ClassName.bestGuess("com.rnett.plugin.ResolvedProperty")
    val ResolvedEnumEntry = ClassName.bestGuess("com.rnett.plugin.ResolvedEnumEntry")

    val OpaqueAnnotationInstance = ClassName.bestGuess("com.rnett.plugin.OpaqueAnnotationInstance")
    val EnumInstance = ClassName.bestGuess("com.rnett.plugin.EnumInstance")

    val String = STRING

    val Name = ClassName.bestGuess("org.jetbrains.kotlin.name.Name")
    val FqName = ClassName.bestGuess("org.jetbrains.kotlin.name.FqName")
    val IdSignaturePublicSignature = ClassName.bestGuess("org.jetbrains.kotlin.ir.util.IdSignature.PublicSignature")
    val IrPluginContext = ClassName.bestGuess("org.jetbrains.kotlin.backend.common.extensions.IrPluginContext")
    val IrBuilderWithScope = ClassName.bestGuess("org.jetbrains.kotlin.ir.builders.IrBuilderWithScope")


    val IrSimpleFunctionSymbol = ClassName.bestGuess("org.jetbrains.kotlin.ir.symbols.IrSimpleFunctionSymbol")
    val IrFieldSymbol = ClassName.bestGuess("org.jetbrains.kotlin.ir.symbols.IrFieldSymbol")
    val IrClassSymbol = ClassName.bestGuess("org.jetbrains.kotlin.ir.symbols.IrClassSymbol")
    val IrTypeAliasSymbol = ClassName.bestGuess("org.jetbrains.kotlin.ir.symbols.IrTypeAliasSymbol")
    val IrPropertySymbol = ClassName.bestGuess("org.jetbrains.kotlin.ir.symbols.IrPropertySymbol")
    val IrConstructorSymbol = ClassName.bestGuess("org.jetbrains.kotlin.ir.symbols.IrConstructorSymbol")
    val IrEnumEntrySymbol = ClassName.bestGuess("org.jetbrains.kotlin.ir.symbols.IrEnumEntrySymbol")

    val IrType = ClassName.bestGuess("org.jetbrains.kotlin.ir.types.IrType")
    val IrSimpleType = ClassName.bestGuess("org.jetbrains.kotlin.ir.types.IrSimpleType")
    val IrTypeArgument = ClassName.bestGuess("org.jetbrains.kotlin.ir.types.IrTypeArgument")
    val IrExpression = ClassName.bestGuess("org.jetbrains.kotlin.ir.expressions.IrExpression")
    val IrCall = ClassName.bestGuess("org.jetbrains.kotlin.ir.expressions.IrCall")
    val IrConstructorCall = ClassName.bestGuess("org.jetbrains.kotlin.ir.expressions.IrConstructorCall")
    val IrProperty = ClassName.bestGuess("org.jetbrains.kotlin.ir.declarations.IrProperty")
    val IrEnumEntry = ClassName.bestGuess("org.jetbrains.kotlin.ir.declarations.IrEnumEntry")

    val IrStarProjectionImpl = ClassName.bestGuess("org.jetbrains.kotlin.ir.types.impl.IrStarProjectionImpl")

    val irCall = MemberName("org.jetbrains.kotlin.ir.builders", "irCall")
    val isGetter = MemberName("org.jetbrains.kotlin.ir.util", "isGetter")
    val isSetter = MemberName("org.jetbrains.kotlin.ir.util", "isSetter")
    val irCallConstructor = MemberName("org.jetbrains.kotlin.ir.builders", "irCallConstructor")
    val irVararg = MemberName("org.jetbrains.kotlin.ir.builders", "irVararg")
    val irCallTypeSubstitutionMap = MemberName("org.jetbrains.kotlin.ir.util", "typeSubstitutionMap")
    val substituteTypes = MemberName("org.jetbrains.kotlin.ir.util", "substitute")
    val typeWith = MemberName("org.jetbrains.kotlin.ir.types", "typeWith")
    val typeWithArguments = MemberName("org.jetbrains.kotlin.ir.types", "typeWithArguments")
    val constructedClass = MemberName("org.jetbrains.kotlin.ir.util", "constructedClass")
    val constructedClassType = MemberName("org.jetbrains.kotlin.ir.util", "constructedClassType")

    val isNative = MemberName("org.jetbrains.kotlin.platform.konan", "isNative")
    val isJs = MemberName("org.jetbrains.kotlin.platform.js", "isJs")
    val isJvm = MemberName("org.jetbrains.kotlin.platform.jvm", "isJvm")

    fun referenceType(declaration: ExportDeclaration): ClassName = when (declaration) {
        is ExportDeclaration.Class -> ClassReference
        is ExportDeclaration.Constructor -> ConstructorReference
        is ExportDeclaration.Function -> FunctionReference
        is ExportDeclaration.Property -> PropertyReference
        is ExportDeclaration.Typealias -> TypealiasReference
    }

    fun resolvedType(declaration: ExportDeclaration): ClassName = when (declaration) {
        is ExportDeclaration.Class -> ResolvedClass
        is ExportDeclaration.Constructor -> ResolvedConstructor
        is ExportDeclaration.Function -> ResolvedFunction
        is ExportDeclaration.Property -> ResolvedProperty
        is ExportDeclaration.Typealias -> ResolvedTypealias
    }

    fun symbolType(declaration: ExportDeclaration): ClassName = when (declaration) {
        is ExportDeclaration.Class -> IrClassSymbol
        is ExportDeclaration.Constructor -> IrConstructorSymbol
        is ExportDeclaration.Function -> IrSimpleFunctionSymbol
        is ExportDeclaration.Property -> IrPropertySymbol
        is ExportDeclaration.Typealias -> IrTypeAliasSymbol
    }
}