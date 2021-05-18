package com.rnett.plugin.generator

import com.rnett.plugin.ResolvedName
import com.rnett.plugin.Signature
import com.squareup.kotlinpoet.CodeBlock

internal fun ResolvedName.toFqName(): CodeBlock = CodeBlock.of("%T(%S)", References.FqName, this.fqName)

internal fun Signature.toIdSignature(): CodeBlock =
    CodeBlock.of("%T(%S, %S, %L, %L)", References.IdSignaturePublicSignature, this.packageFqName, this.declarationFqName, this.id, this.mask)