package com.rnett.plugin.generator

import com.rnett.plugin.ConstantValue
import com.rnett.plugin.ExportDeclaration
import com.rnett.plugin.ResolvedName
import com.rnett.plugin.Signature
import com.squareup.kotlinpoet.BOOLEAN
import com.squareup.kotlinpoet.BYTE
import com.squareup.kotlinpoet.CHAR
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.DOUBLE
import com.squareup.kotlinpoet.FLOAT
import com.squareup.kotlinpoet.INT
import com.squareup.kotlinpoet.LONG
import com.squareup.kotlinpoet.NOTHING
import com.squareup.kotlinpoet.SHORT
import com.squareup.kotlinpoet.STRING
import com.squareup.kotlinpoet.TypeName

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
