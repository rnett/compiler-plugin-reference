package com.rnett.plugin.generator

import com.rnett.plugin.AnnotationArgument
import com.rnett.plugin.AnnotationParameter
import com.rnett.plugin.ConstantValue
import com.rnett.plugin.ExportDeclaration
import com.rnett.plugin.ResolvedName
import com.rnett.plugin.Signature
import com.squareup.kotlinpoet.BOOLEAN
import com.squareup.kotlinpoet.BYTE
import com.squareup.kotlinpoet.CHAR
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.DOUBLE
import com.squareup.kotlinpoet.FLOAT
import com.squareup.kotlinpoet.INT
import com.squareup.kotlinpoet.LONG
import com.squareup.kotlinpoet.NOTHING
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.SHORT
import com.squareup.kotlinpoet.STRING
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.asClassName

internal fun ResolvedName.toFqName(): CodeBlock = CodeBlock.of("%T(%S)", References.FqName, this.fqName)

internal fun Signature.toIdSignature(): CodeBlock =
    CodeBlock.of(
        "%T(%S, %S, %L, %L)",
        References.IdSignaturePublicSignature,
        this.packageFqName,
        this.declarationFqName,
        this.id,
        this.mask
    )

internal inline fun <T> CodeBlock.Builder.addBlock(
    header: String,
    items: Collection<T>,
    newLine: Boolean = true,
    block: CodeBlock.Builder.(T) -> Unit
) = apply {
    if (items.isNotEmpty()) {
        addStatement(header)
        items.forEach {
            block(it)
        }
        if (newLine)
            add("\n")
    }
}

internal inline fun <T> CodeBlock.Builder.addListBlock(
    header: String,
    items: Collection<T>,
    newLine: Boolean = true,
    literal: (T) -> String
) =
    addBlock(header, items, newLine) {
        addStatement("* %L", literal(it))
    }

internal fun ExportDeclaration.TypeParameter.classKdoc() = buildString {
    append("`")
    append(variance.prefix)
    append(name)
    if (supertypes.isNotEmpty()) {
        append(" : ")
        append(supertypes.joinToString(", "))
    }
    append("`")
}

internal fun ExportDeclaration.Param.classKdoc() = buildString {
    append("`")
    if (varargs)
        append("vararg ")
    append("$name: $type")
    if (optional)
        append(" = ...")
    append("`")
}

internal fun ExportDeclaration.TypeParameter.callKdoc() = buildString {
    append("`")
    append(variance.prefix)
    append("?")
    if (supertypes.isNotEmpty()) {
        append(" : ")
        append(supertypes.joinToString(", "))
    }
    append("`")
}

internal fun ExportDeclaration.Param.callKdoc() = buildString {
    append("`")
    if (varargs)
        append("vararg ")
    append("$type")
    if (optional)
        append(" = ...")
    append("`")
}

internal val ConstantValue.Kind.valueType: TypeName
    get() = when (this) {
        is ConstantValue.Kind.Boolean -> BOOLEAN
        is ConstantValue.Kind.Byte -> BYTE
        is ConstantValue.Kind.Char -> CHAR
        is ConstantValue.Kind.Double -> DOUBLE
        is ConstantValue.Kind.Float -> FLOAT
        is ConstantValue.Kind.Int -> INT
        is ConstantValue.Kind.Long -> LONG
        is ConstantValue.Kind.Null -> NOTHING.copy(nullable = true)
        is ConstantValue.Kind.Short -> SHORT
        is ConstantValue.Kind.String -> STRING
    }

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
        is AnnotationArgument.Array -> TODO()
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

/**
 * Requires `context` in scope?
 * TODO implement
 */
internal fun AnnotationParameter.value(
    context: String,
    annotation: String,
    classNameForFqName: (ResolvedName) -> ClassName
): CodeBlock {
    val argument = "${annotation}.getValueArgument($index)"

    fun standard(
        type: CodeBlock,
        transform: CodeBlock,
        default: (AnnotationArgument) -> CodeBlock = { it.value(context, classNameForFqName) }
    ): CodeBlock = if (this.default != null) {
        CodeBlock.of("$argument?.%M<%L>()?.let{ %L } ?: %L", References.cast, type, transform, default(this.default!!))
    } else {
        CodeBlock.of("$argument!!.%M<%L>().let{ %L }", References.cast, type, transform)
    }

    return when (val kind = this.kind) {
        is AnnotationArgument.Kind.Array -> CodeBlock.of("TODO()") // TODO
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