package com.rnett.plugin

import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.descriptors.*
import org.jetbrains.kotlin.ir.ObsoleteDescriptorBasedAPI
import org.jetbrains.kotlin.ir.declarations.*
import org.jetbrains.kotlin.ir.symbols.*
import org.jetbrains.kotlin.ir.util.IdSignature
import org.jetbrains.kotlin.name.FqName

class SignatureMismatchException(val reference: Reference<*, *, *, *>, val symbol: IrSymbol) :
    RuntimeException(
        "Signature mismatch for reference $reference with symbol $symbol: trying to import ${reference.signature}, but found ${symbol.signature}"
    )

@OptIn(ObsoleteDescriptorBasedAPI::class)
class ReferenceNotFoundException(val reference: Reference<*, *, *, *>, val context: IrPluginContext) :
    RuntimeException("No declaration found for reference $reference from module ${context.moduleDescriptor.name}")

class MultipleMatchesException(val reference: Reference<*, *, *, *>, val matches: List<IrSymbol>) :
    RuntimeException("Multiple matching symbols found for reference $reference with the same signature: $matches")

sealed interface Reference<S : IrBindableSymbol<D, T>, T : IrDeclaration, D : DeclarationDescriptor, R : ResolvedReference<S, T, D>> {
    val fqName: FqName
    val signature: IdSignature.PublicSignature

    @InternalPluginReferenceAPI
    fun doResolve(context: IrPluginContext): S?

    fun resolveSymbolOrNull(context: IrPluginContext): S? = doResolve(context)?.checkSignature()
    fun resolveSymbol(context: IrPluginContext): S =
        resolveSymbolOrNull(context) ?: throw ReferenceNotFoundException(this, context)

    fun getResolvedReference(context: IrPluginContext, symbol: S): R

    fun resolveOrNull(context: IrPluginContext): R? =
        resolveSymbolOrNull(context)?.let { getResolvedReference(context, it) }

    fun resolve(context: IrPluginContext): R = getResolvedReference(context, resolveSymbol(context))

    operator fun invoke(context: IrPluginContext): R = resolve(context)

    @InternalPluginReferenceAPI
    fun S.checkSignature(): S = apply {
        if (this.signature != this@Reference.signature)
            throw SignatureMismatchException(this@Reference, this)
    }

    @InternalPluginReferenceAPI
    fun Iterable<S>.findWithSignature(): S? {
        val matching = filter { it.signature == this@Reference.signature }
        return when {
            matching.isEmpty() -> null
            matching.size > 1 -> throw MultipleMatchesException(this@Reference, matching)
            else -> matching[0]
        }
    }

}

abstract class ClassReference<R : ResolvedClass>(
    override val fqName: FqName,
    override val signature: IdSignature.PublicSignature
) :
    Reference<IrClassSymbol, IrClass, ClassDescriptor, R>, AnnotationArgument {
    override fun doResolve(context: IrPluginContext): IrClassSymbol? = context.referenceClass(fqName)?.checkSignature()
}

abstract class TypealiasReference<R : ResolvedTypealias>(
    override val fqName: FqName,
    override val signature: IdSignature.PublicSignature
) :
    Reference<IrTypeAliasSymbol, IrTypeAlias, TypeAliasDescriptor, R> {
    override fun doResolve(context: IrPluginContext): IrTypeAliasSymbol? = context.referenceTypeAlias(fqName)
}

abstract class ConstructorReference<R : ResolvedConstructor>(
    override val fqName: FqName,
    override val signature: IdSignature.PublicSignature
) :
    Reference<IrConstructorSymbol, IrConstructor, ClassConstructorDescriptor, R> {
    override fun doResolve(context: IrPluginContext): IrConstructorSymbol? =
        context.referenceConstructors(fqName).findWithSignature()
}

abstract class FunctionReference<R : ResolvedFunction>(
    override val fqName: FqName,
    override val signature: IdSignature.PublicSignature
) :
    Reference<IrSimpleFunctionSymbol, IrSimpleFunction, FunctionDescriptor, R> {
    override fun doResolve(context: IrPluginContext): IrSimpleFunctionSymbol? =
        context.referenceFunctions(fqName).findWithSignature()
}

abstract class PropertyReference<R : ResolvedProperty>(
    override val fqName: FqName,
    override val signature: IdSignature.PublicSignature
) :
    Reference<IrPropertySymbol, IrProperty, PropertyDescriptor, R> {
    override fun doResolve(context: IrPluginContext): IrPropertySymbol? =
        context.referenceProperties(fqName).findWithSignature()
}
