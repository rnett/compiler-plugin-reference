package com.rnett.plugin

import org.jetbrains.kotlin.ir.declarations.IrEnumEntry
import org.jetbrains.kotlin.ir.expressions.IrClassReference
import org.jetbrains.kotlin.ir.expressions.IrConst
import org.jetbrains.kotlin.ir.expressions.IrConstructorCall
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrFunctionAccessExpression
import org.jetbrains.kotlin.ir.expressions.IrGetEnumValue
import org.jetbrains.kotlin.ir.expressions.IrVararg
import org.jetbrains.kotlin.ir.types.classFqName
import org.jetbrains.kotlin.ir.util.constructedClass
import org.jetbrains.kotlin.ir.util.dump
import org.jetbrains.kotlin.ir.util.kotlinFqName
import org.jetbrains.kotlin.ir.util.parentAsClass
import org.jetbrains.kotlin.name.FqName

sealed interface AnnotationArgument

internal fun AnnotationArgument(expression: IrExpression): AnnotationArgument =
    when (expression) {
        is IrConstructorCall -> OpaqueAnnotationInstance(expression)
        is IrConst<*> -> OpaqueConstant(expression.value)
        is IrGetEnumValue -> OpaqueEnumEntry(expression.symbol.owner.parentAsClass.kotlinFqName,
            expression.symbol.owner.name.asString(),
            expression.symbol.owner.parentAsClass.declarations.filterIsInstance<IrEnumEntry>()
                .indexOfFirst { it.symbol == expression.symbol })
        is IrVararg -> OpaqueArray(expression.elements.map { AnnotationArgument(it as IrExpression) })
        is IrClassReference -> OpaqueClassRef(expression.classType.classFqName!!)
        else -> error("Unknown annotation argument: ${expression.dump(true)}")
    }

internal fun IrFunctionAccessExpression.valueArgumentsByName(): Map<String, IrExpression> {
    val byIndex = (0 until valueArgumentsCount).associateWith { getValueArgument(it) }
    val names = symbol.owner.valueParameters.map { it.name }
    return byIndex.mapKeys { names[it.key].asString() }.filterValues { it != null } as Map<String, IrExpression>
}

//TODO do I want full opaque argument types?  I probably should, annoying to type though
data class OpaqueAnnotationInstance(val fqName: FqName, val arguments: Map<String, AnnotationArgument>) :
    AnnotationArgument {
    constructor(call: IrConstructorCall) : this(
        call.symbol.owner.constructedClass.kotlinFqName,
        call.valueArgumentsByName().mapValues {
            AnnotationArgument(it.value)
        })
}

data class OpaqueEnumEntry(val classFqName: FqName, val name: String, val ordinal: Int) : AnnotationArgument

data class OpaqueClassRef(val fqName: FqName) : AnnotationArgument

data class OpaqueArray(val elements: List<AnnotationArgument>) : AnnotationArgument,
    List<AnnotationArgument> by elements

//TODO types
data class OpaqueConstant(val value: Any?) : AnnotationArgument