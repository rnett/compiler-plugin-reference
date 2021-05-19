package com.rnett.plugin.generator

import com.rnett.plugin.ExportDeclaration
import com.rnett.plugin.ResolvedName
import com.rnett.plugin.Signature
import com.squareup.kotlinpoet.CodeBlock

internal fun ResolvedName.toFqName(): CodeBlock = CodeBlock.of("%T(%S)", References.FqName, this.fqName)

internal fun Signature.toIdSignature(): CodeBlock =
    CodeBlock.of("%T(%S, %S, %L, %L)", References.IdSignaturePublicSignature, this.packageFqName, this.declarationFqName, this.id, this.mask)

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

internal inline fun <T> CodeBlock.Builder.addListBlock(header: String, items: Collection<T>, newLine: Boolean = true, literal: (T) -> String) =
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