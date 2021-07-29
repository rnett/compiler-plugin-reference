package com.rnett.plugin

import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.descriptors.ClassDescriptor
import org.jetbrains.kotlin.ir.builders.IrBuilderWithScope
import org.jetbrains.kotlin.ir.declarations.IrEnumEntry
import org.jetbrains.kotlin.ir.expressions.impl.IrGetEnumValueImpl
import org.jetbrains.kotlin.ir.symbols.IrEnumEntrySymbol
import org.jetbrains.kotlin.ir.util.defaultType
import org.jetbrains.kotlin.ir.util.parentAsClass
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name

fun IrPluginContext.referenceEnumEntry(fqName: FqName): IrEnumEntrySymbol? = referenceClass(fqName.parent())
    ?.let {
        it.owner.declarations
            .filterIsInstance<IrEnumEntry>()
            .map { it.symbol }
            .firstOrNull { it.owner.name == fqName.shortName() }
    }

fun IrPluginContext.referenceEnumEntry(classFqName: FqName, name: Name): IrEnumEntrySymbol? =
    referenceClass(classFqName)
        ?.let {
            it.owner.declarations
                .filterIsInstance<IrEnumEntry>()
                .map { it.symbol }
                .firstOrNull { it.owner.name == name }
        }

interface EnumEntryReference<E : ResolvedEnumEntry<E>, C : ResolvedClass> :
    Reference<IrEnumEntrySymbol, IrEnumEntry, ClassDescriptor, E> {
    val classReference: ClassReference<C>
    val name: String
    val ordinal: Int

    override fun doResolve(context: IrPluginContext): IrEnumEntrySymbol? =
        (context.referenceClass(classReference.fqName) ?: error("Enum class ${classReference.fqName} not found"))
            .owner.declarations
            .filterIsInstance<IrEnumEntry>()
            .map { it.symbol }
            .findWithSignature()

    override val fqName: FqName get() = classReference.fqName.child(Name.guessByFirstCharacter(name))
}

open class ResolvedEnumEntry<E : ResolvedEnumEntry<E>>(val entry: EnumEntryReference<E, *>, symbol: IrEnumEntrySymbol) :
    ResolvedReference<IrEnumEntrySymbol, IrEnumEntry, ClassDescriptor>(symbol, entry.fqName), IrEnumEntrySymbol {

    fun get(builder: IrBuilderWithScope) =
        IrGetEnumValueImpl(builder.startOffset, builder.endOffset, symbol.owner.parentAsClass.defaultType, symbol)

    override fun toString(): String {
        return "ResolvedEnumEntry(fqName=$fqName, symbol=$symbol)"
    }
}