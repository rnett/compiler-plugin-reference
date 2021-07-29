package com.rnett.plugin.generator

import com.rnett.plugin.AnnotationArgument
import com.rnett.plugin.AnnotationParameter
import com.rnett.plugin.ResolvedName
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.asClassName


internal fun AnnotationArgument.Kind.valueType(classNameForFqName: (ResolvedName) -> ClassName): TypeName =
    when (this) {
        is AnnotationArgument.Kind.ExportedAnnotation -> classNameForFqName(this.fqName).nestedClass("Instance")
        is AnnotationArgument.Kind.OpaqueAnnotation -> References.OpaqueAnnotationInstance
        is AnnotationArgument.Kind.Array -> List::class.asClassName()
            .parameterizedBy(elementKind.valueType(classNameForFqName))
        is AnnotationArgument.Kind.ClassRef -> References.IrClassSymbol
        is AnnotationArgument.Kind.Constant -> this.valueKind.valueType
        is AnnotationArgument.Kind.Enum -> References.IrEnumEntrySymbol
    }

internal fun AnnotationArgument.asOpaque(): CodeBlock = when (this) {
    is AnnotationArgument.Array -> CodeBlock.of(
        "%T(${this.values.joinToString(", ") { "%L" }})",
        References.OpaqueArray,
        *this.values.map { it.asOpaque() }.toTypedArray()
    )
    is AnnotationArgument.ClassRef -> CodeBlock.of(
        "%T(%T(%S))",
        References.OpaqueClassRef,
        References.FqName,
        fqName.fqName
    )
    is AnnotationArgument.Constant -> CodeBlock.of("%T(${value.valueAsString()})", References.OpaqueConstant)
    is AnnotationArgument.Enum -> CodeBlock.of(
        "%T(%T(%S), %S, %L)",
        References.OpaqueEnumEntry,
        References.FqName,
        classFqName.fqName,
        name,
        ordinal
    )
    is AnnotationArgument.Annotation -> CodeBlock.of("%T(%T(%S), mapOf(${
        arguments.toList().joinToString(", ") { "%L" }
    }))", References.OpaqueAnnotationInstance, References.FqName, fqName.fqName, *arguments.map {
        CodeBlock.of("%S to %L", it.key, it.value.asOpaque())
    }.toTypedArray()
    )
}

internal fun AnnotationArgument.value(context: String, classNameForFqName: (ResolvedName) -> ClassName): CodeBlock =
    when (this) {
        is AnnotationArgument.Array -> CodeBlock.of(
            "listOf(${this.values.joinToString(", ") { "%L" }})",
            *values.map { it.value(context, classNameForFqName) }.toTypedArray()
        )
        is AnnotationArgument.ClassRef -> CodeBlock.of(
            "$context.referenceClass(%T(%S))!!",
            References.FqName,
            fqName.fqName
        )
        is AnnotationArgument.Constant -> CodeBlock.of(this.value.valueAsString())
        is AnnotationArgument.Enum -> CodeBlock.of(
            "$context.%M(%T(%S), %T.identifier(%S))!!",
            References.referenceEnumEntry,
            References.FqName,
            classFqName.fqName,
            References.Name,
            this.name
        )
        is AnnotationArgument.ExportedAnnotation -> CodeBlock.of("%T.Instance(${
            arguments.toList().joinToString(", ") {
                "${it.first} = ${it.second.value(context, classNameForFqName)}"
            }
        })", classNameForFqName(fqName))
        is AnnotationArgument.OpaqueAnnotation -> asOpaque()
    }

internal fun value(
    argument: String,
    context: String,
    default: AnnotationArgument?,
    kind: AnnotationArgument.Kind,
    classNameForFqName: (ResolvedName) -> ClassName,
): CodeBlock {
    fun standard(
        type: CodeBlock,
        transform: CodeBlock,
        defaultTransform: (AnnotationArgument) -> CodeBlock = { it.value(context, classNameForFqName) }
    ): CodeBlock = if (default != null) {
        CodeBlock.of(
            "$argument?.%M<%L>()?.let{ %L } ?: %L",
            References.cast,
            type,
            transform,
            defaultTransform(default)
        )
    } else {
        CodeBlock.of("$argument!!.%M<%L>().let{ %L }", References.cast, type, transform)
    }

    return when (kind) {
        is AnnotationArgument.Kind.Array -> standard(
            CodeBlock.of("%T", References.IrVararg), CodeBlock.of(
                "it.elements.map { %L }",
                value(
                    "it",
                    context,
                    null,
                    kind.elementKind,
                    classNameForFqName
                )
            )
        )
        is AnnotationArgument.Kind.ClassRef -> standard(
            CodeBlock.of("%T", References.IrClassReference),
            CodeBlock.of("it.symbol as IrClassSymbol")
        )
        is AnnotationArgument.Kind.Constant -> standard(
            CodeBlock.of("%T<${kind.valueKind.name}>", References.IrConst),
            CodeBlock.of("it.value")
        )
        is AnnotationArgument.Kind.Enum -> standard(
            CodeBlock.of("%T", References.IrGetEnumValue),
            CodeBlock.of("it.symbol")
        )
        is AnnotationArgument.Kind.ExportedAnnotation -> standard(
            CodeBlock.of("%T", References.IrConstructorCall),
            CodeBlock.of("%T.Instance(it, context)", classNameForFqName(kind.fqName))
        )
        is AnnotationArgument.Kind.OpaqueAnnotation -> standard(
            CodeBlock.of("%T", References.IrConstructorCall),
            CodeBlock.of("%T(it)", References.OpaqueAnnotationInstance)
        )
    }
}

/**
 * Requires `context` in scope?
 */
internal fun AnnotationParameter.value(
    context: String,
    annotation: String,
    classNameForFqName: (ResolvedName) -> ClassName,
): CodeBlock {
    return value("${annotation}.getValueArgument($index)", context, this.default, this.kind, classNameForFqName)
}