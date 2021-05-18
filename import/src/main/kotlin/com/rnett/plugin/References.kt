package com.rnett.plugin

import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.ObsoleteDescriptorBasedAPI
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrConstructor
import org.jetbrains.kotlin.ir.declarations.IrDeclaration
import org.jetbrains.kotlin.ir.declarations.IrProperty
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.declarations.IrTypeAlias
import org.jetbrains.kotlin.ir.symbols.IrBindableSymbol
import org.jetbrains.kotlin.ir.symbols.IrClassSymbol
import org.jetbrains.kotlin.ir.symbols.IrConstructorSymbol
import org.jetbrains.kotlin.ir.symbols.IrPropertySymbol
import org.jetbrains.kotlin.ir.symbols.IrSimpleFunctionSymbol
import org.jetbrains.kotlin.ir.symbols.IrSymbol
import org.jetbrains.kotlin.ir.symbols.IrTypeAliasSymbol
import org.jetbrains.kotlin.ir.util.IdSignature
import org.jetbrains.kotlin.name.FqName

class SignatureMismatchException(val reference: Reference<*, *, *>, val symbol: IrSymbol) :
    RuntimeException(
        "Signature mismatch for reference $reference with symbol $symbol: trying to import ${reference.signature}, but found ${symbol.signature}"
    )

@OptIn(ObsoleteDescriptorBasedAPI::class)
class ReferenceNotFoundException(val reference: Reference<*, *, *>, val context: IrPluginContext) :
    RuntimeException("No declaration found for reference $reference from module ${context.moduleDescriptor.name}")

class MultipleMatchesException(val reference: Reference<*, *, *>, val matches: List<IrSymbol>) :
    RuntimeException("Multiple matching symbols found for reference $reference with the same signature: $matches")

sealed class Reference<S : IrBindableSymbol<*, T>, T : IrDeclaration, R : ResolvedReference<S, T>> {
    abstract val fqName: FqName
    abstract val signature: IdSignature.PublicSignature

    protected abstract fun doResolve(context: IrPluginContext): S?

    fun resolveSymbolOrNull(context: IrPluginContext): S? = doResolve(context)?.checkSignature()
    fun resolveSymbol(context: IrPluginContext): S = resolveSymbolOrNull(context) ?: throw ReferenceNotFoundException(this, context)

    protected abstract fun getResolvedReference(symbol: S): R

    fun resolveOrNull(context: IrPluginContext): R? = resolveSymbolOrNull(context)?.let(::getResolvedReference)
    fun resolve(context: IrPluginContext): R = resolveSymbol(context).let(::getResolvedReference)

    protected fun S.checkSignature(): S = apply {
        if (this.signature != this@Reference.signature)
            throw SignatureMismatchException(this@Reference, this)
    }

    protected fun Iterable<S>.findWithSignature(): S? {
        val matching = filter { it.signature == this@Reference.signature }
        return when {
            matching.isEmpty() -> null
            matching.size > 1 -> throw MultipleMatchesException(this@Reference, matching)
            else -> matching[0]
        }
    }

}

//TODO use pluginContext.referenceTopLevel()?  Can use IdSignature.asPublic() for resolution.  Will it work with @PublishedAPI?
open class ClassReference(override val fqName: FqName, override val signature: IdSignature.PublicSignature) :
    Reference<IrClassSymbol, IrClass, ResolvedClass>() {
    override fun doResolve(context: IrPluginContext): IrClassSymbol? = context.referenceClass(fqName)?.checkSignature()

    override fun getResolvedReference(symbol: IrClassSymbol): ResolvedClass = ResolvedClass(symbol, fqName)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ClassReference) return false

        if (fqName != other.fqName) return false
        if (signature != other.signature) return false

        return true
    }

    override fun hashCode(): Int {
        var result = fqName.hashCode()
        result = 31 * result + signature.hashCode()
        return result
    }

    override fun toString(): String {
        return "ClassReference(fqName=$fqName, signature=$signature)"
    }
}

data class TypealiasReference(override val fqName: FqName, override val signature: IdSignature.PublicSignature) :
    Reference<IrTypeAliasSymbol, IrTypeAlias, ResolvedTypealias>() {
    override fun doResolve(context: IrPluginContext): IrTypeAliasSymbol? = context.referenceTypeAlias(fqName)
    override fun getResolvedReference(symbol: IrTypeAliasSymbol): ResolvedTypealias = ResolvedTypealias(symbol, fqName)
}

data class ConstructorReference(override val fqName: FqName, override val signature: IdSignature.PublicSignature) :
    Reference<IrConstructorSymbol, IrConstructor, ResolvedConstructor>() {
    override fun doResolve(context: IrPluginContext): IrConstructorSymbol? = context.referenceConstructors(fqName).findWithSignature()
    override fun getResolvedReference(symbol: IrConstructorSymbol): ResolvedConstructor = ResolvedConstructor(symbol, fqName)
}

data class FunctionReference(override val fqName: FqName, override val signature: IdSignature.PublicSignature) :
    Reference<IrSimpleFunctionSymbol, IrSimpleFunction, ResolvedFunction>() {
    override fun doResolve(context: IrPluginContext): IrSimpleFunctionSymbol? = context.referenceFunctions(fqName).findWithSignature()
    override fun getResolvedReference(symbol: IrSimpleFunctionSymbol): ResolvedFunction = ResolvedFunction(symbol, fqName)
}

data class PropertyReference(override val fqName: FqName, override val signature: IdSignature.PublicSignature) :
    Reference<IrPropertySymbol, IrProperty, ResolvedProperty>() {
    override fun doResolve(context: IrPluginContext): IrPropertySymbol? = context.referenceProperties(fqName).findWithSignature()
    override fun getResolvedReference(symbol: IrPropertySymbol): ResolvedProperty = ResolvedProperty(symbol, fqName)
}
