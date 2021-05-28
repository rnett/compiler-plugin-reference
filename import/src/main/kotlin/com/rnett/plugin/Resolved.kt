package com.rnett.plugin

import org.jetbrains.kotlin.ir.builders.IrBuilderWithScope
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrConstructor
import org.jetbrains.kotlin.ir.declarations.IrDeclaration
import org.jetbrains.kotlin.ir.declarations.IrEnumEntry
import org.jetbrains.kotlin.ir.declarations.IrProperty
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.declarations.IrTypeAlias
import org.jetbrains.kotlin.ir.expressions.impl.IrGetEnumValueImpl
import org.jetbrains.kotlin.ir.symbols.IrBindableSymbol
import org.jetbrains.kotlin.ir.symbols.IrClassSymbol
import org.jetbrains.kotlin.ir.symbols.IrConstructorSymbol
import org.jetbrains.kotlin.ir.symbols.IrEnumEntrySymbol
import org.jetbrains.kotlin.ir.symbols.IrPropertySymbol
import org.jetbrains.kotlin.ir.symbols.IrSimpleFunctionSymbol
import org.jetbrains.kotlin.ir.symbols.IrSymbol
import org.jetbrains.kotlin.ir.symbols.IrTypeAliasSymbol
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.util.defaultType
import org.jetbrains.kotlin.ir.util.parentAsClass
import org.jetbrains.kotlin.name.FqName

sealed class ResolvedReference<S : IrBindableSymbol<*, T>, T : IrDeclaration>(val symbol: S, val fqName: FqName) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (symbol === other) return true
        if (other is IrSymbol && symbol == other) return true
        return false
    }

    override fun hashCode(): Int {
        return symbol.hashCode()
    }
}

open class ResolvedClass(symbol: IrClassSymbol, fqName: FqName) : ResolvedReference<IrClassSymbol, IrClass>(symbol, fqName),
    IrClassSymbol by symbol {

    override fun toString(): String {
        return "ResolvedClass(fqName=$fqName, symbol=$symbol)"
    }
}

open class ResolvedTypealias(symbol: IrTypeAliasSymbol, fqName: FqName) :
    ResolvedReference<IrTypeAliasSymbol, IrTypeAlias>(symbol, fqName), IrTypeAliasSymbol by symbol {
    val expandedType: IrType get() = owner.expandedType

    override fun toString(): String {
        return "ResolvedTypealias(fqName=$fqName, symbol=$symbol)"
    }
}

open class ResolvedConstructor(symbol: IrConstructorSymbol, fqName: FqName) :
    ResolvedReference<IrConstructorSymbol, IrConstructor>(symbol, fqName), IrConstructorSymbol by symbol {

    override fun toString(): String {
        return "ResolvedConstructor(fqName=$fqName, symbol=$symbol)"
    }
}

open class ResolvedFunction(symbol: IrSimpleFunctionSymbol, fqName: FqName) :
    ResolvedReference<IrSimpleFunctionSymbol, IrSimpleFunction>(symbol, fqName), IrSimpleFunctionSymbol by symbol {

    override fun toString(): String {
        return "ResolvedFunction(fqName=$fqName, symbol=$symbol)"
    }
}

open class ResolvedProperty(symbol: IrPropertySymbol, fqName: FqName) :
    ResolvedReference<IrPropertySymbol, IrProperty>(symbol, fqName), IrPropertySymbol by symbol {

    override fun toString(): String {
        return "ResolvedProperty(fqName=$fqName, symbol=$symbol)"
    }
}

open class ResolvedEnumEntry(val ordinal: Int, symbol: IrEnumEntrySymbol, fqName: FqName) :
    ResolvedReference<IrEnumEntrySymbol, IrEnumEntry>(symbol, fqName), IrEnumEntrySymbol by symbol {

    fun get(builder: IrBuilderWithScope) = IrGetEnumValueImpl(builder.startOffset, builder.endOffset, symbol.owner.parentAsClass.defaultType, symbol)

    val name: String = fqName.shortName().asString()

    override fun toString(): String {
        return "ResolvedEnumEntry(fqName=$fqName, symbol=$symbol)"
    }
}