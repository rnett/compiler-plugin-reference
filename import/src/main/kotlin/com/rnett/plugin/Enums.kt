package com.rnett.plugin

import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.builders.IrBuilderWithScope
import org.jetbrains.kotlin.ir.declarations.IrEnumEntry
import org.jetbrains.kotlin.ir.expressions.impl.IrGetEnumValueImpl
import org.jetbrains.kotlin.ir.symbols.IrEnumEntrySymbol
import org.jetbrains.kotlin.ir.util.IdSignature
import org.jetbrains.kotlin.ir.util.defaultType
import org.jetbrains.kotlin.ir.util.parentAsClass
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name

interface EnumInstance : AnnotationArgument {
    val name: String
    val ordinal: Int
}

class EnumEntryReference<E : EnumInstance>(val classFqName: FqName, instanceGetter: () -> E, override val signature: IdSignature.PublicSignature) :
    Reference<IrEnumEntrySymbol, IrEnumEntry, ResolvedEnumEntry<E>>() {
    override fun doResolve(context: IrPluginContext): IrEnumEntrySymbol? =
        (context.referenceClass(classFqName) ?: error("Enum class $classFqName not found"))
            .owner.declarations
            .filterIsInstance<IrEnumEntry>()
            .map { it.symbol }
            .findWithSignature()

    val instance: E by lazy { instanceGetter() }
    val name by lazy { instance.name }
    val ordinal by lazy { instance.ordinal }

    override fun getResolvedReference(context: IrPluginContext, symbol: IrEnumEntrySymbol): ResolvedEnumEntry<E> = ResolvedEnumEntry(this, symbol)
    override val fqName: FqName = classFqName.child(Name.guessByFirstCharacter(name))
}

class ResolvedEnumEntry<E : EnumInstance>(val reference: EnumEntryReference<E>, symbol: IrEnumEntrySymbol) :
    ResolvedReference<IrEnumEntrySymbol, IrEnumEntry>(symbol, reference.fqName), IrEnumEntrySymbol by symbol {

    val instance: E by lazy { reference.instance }

    fun get(builder: IrBuilderWithScope) = IrGetEnumValueImpl(builder.startOffset, builder.endOffset, symbol.owner.parentAsClass.defaultType, symbol)

    val name by lazy { instance.name }
    val ordinal by lazy { instance.ordinal }

    override fun toString(): String {
        return "ResolvedEnumEntry(fqName=$fqName, symbol=$symbol, instance=$instance, ordinal=$ordinal)"
    }
}