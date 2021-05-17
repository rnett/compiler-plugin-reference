package com.rnett.plugin

import org.jetbrains.kotlin.name.FqName

fun FqName.toResolvedName() = ResolvedName(this.asString())

object Names {
    val PluginExport = FqName("com.rnett.plugin.export.annotations.PluginExport")
}