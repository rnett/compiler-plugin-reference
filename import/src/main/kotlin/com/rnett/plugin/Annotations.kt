package com.rnett.plugin

import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.declarations.IrAnnotationContainer
import org.jetbrains.kotlin.ir.expressions.IrConstructorCall
import org.jetbrains.kotlin.ir.util.getAnnotation
import org.jetbrains.kotlin.ir.util.hasAnnotation
import org.jetbrains.kotlin.ir.util.hasEqualFqName
import org.jetbrains.kotlin.ir.util.parentAsClass
import org.jetbrains.kotlin.name.FqName

sealed interface AnnotationReference<out T> {
    val fqName: FqName
    fun invoke(`annotation`: IrConstructorCall, context: IrPluginContext): T
}

interface RepeatableAnnotationReference<T> : AnnotationReference<T>

interface SingleAnnotationReference<T> : AnnotationReference<T>

operator fun <T> IrAnnotationContainer.contains(ref: AnnotationReference<T>) = this.hasAnnotation(ref.fqName)

@JvmName("getSingleAnnotation")
operator fun <T> IrAnnotationContainer.get(ref: SingleAnnotationReference<T>, context: IrPluginContext) =
    getAnnotation(ref.fqName)?.let {
        ref.invoke(it, context)
    }

fun <T> IrAnnotationContainer.getAnnotation(ref: SingleAnnotationReference<T>, context: IrPluginContext) =
    this[ref, context]

@JvmName("getRepeatableAnnotation")
operator fun <T> IrAnnotationContainer.get(ref: RepeatableAnnotationReference<T>, context: IrPluginContext) =
    this.annotations.mapNotNull {
        if (!it.symbol.owner.parentAsClass.hasEqualFqName(ref.fqName)) return@mapNotNull null
        ref.invoke(it, context)
    }

fun <T> IrAnnotationContainer.getAnnotations(ref: RepeatableAnnotationReference<T>, context: IrPluginContext) =
    this[ref, context]