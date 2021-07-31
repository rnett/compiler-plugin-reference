package com.rnett.plugin

import org.jetbrains.kotlin.descriptors.*
import org.jetbrains.kotlin.ir.declarations.*
import org.jetbrains.kotlin.ir.symbols.*
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.name.FqName

sealed class ResolvedReference<S : IrBindableSymbol<D, T>, T : IrDeclaration, D : DeclarationDescriptor>(
    open val symbol: S,
    val fqName: FqName
) :
    IrDelegatingSymbol<S, T, D>(symbol)

open class ResolvedClass(symbol: IrClassSymbol, fqName: FqName) :
    ResolvedReference<IrClassSymbol, IrClass, ClassDescriptor>(symbol, fqName),
    IrClassSymbol {

    override fun toString(): String {
        return "ResolvedClass(fqName=$fqName, symbol=$symbol)"
    }
}

open class ResolvedTypealias(symbol: IrTypeAliasSymbol, fqName: FqName) :
    ResolvedReference<IrTypeAliasSymbol, IrTypeAlias, TypeAliasDescriptor>(symbol, fqName), IrTypeAliasSymbol {
    val expandedType: IrType get() = owner.expandedType

    override fun toString(): String {
        return "ResolvedTypealias(fqName=$fqName, symbol=$symbol)"
    }
}

open class ResolvedConstructor(symbol: IrConstructorSymbol, fqName: FqName) :
    ResolvedReference<IrConstructorSymbol, IrConstructor, ClassConstructorDescriptor>(symbol, fqName),
    IrConstructorSymbol {

    override fun toString(): String {
        return "ResolvedConstructor(fqName=$fqName, symbol=$symbol)"
    }
}

open class ResolvedFunction(symbol: IrSimpleFunctionSymbol, fqName: FqName) :
    ResolvedReference<IrSimpleFunctionSymbol, IrSimpleFunction, FunctionDescriptor>(symbol, fqName),
    IrSimpleFunctionSymbol {

    override fun toString(): String {
        return "ResolvedFunction(fqName=$fqName, symbol=$symbol)"
    }
}

open class ResolvedProperty(symbol: IrPropertySymbol, fqName: FqName) :
    ResolvedReference<IrPropertySymbol, IrProperty, PropertyDescriptor>(symbol, fqName), IrPropertySymbol {

    override fun toString(): String {
        return "ResolvedProperty(fqName=$fqName, symbol=$symbol)"
    }
}