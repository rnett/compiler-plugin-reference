package com.rnett.plugin.generator

import com.rnett.plugin.ResolvedName
import com.squareup.kotlinpoet.ClassName

internal class FqNameLookup(val declarationTree: DeclarationTree, val rootClass: ClassName) {


    private fun DeclarationTree.setupNames(parent: List<String>, map: MutableMap<ResolvedName, ClassName>) {
        val current = parent + displayName
        declaration?.let {
            map[it.referenceFqName] = current.fold(rootClass) { className, it -> className.nestedClass(it) }
        }
        children.forEach { it.setupNames(current, map) }
    }

    @OptIn(ExperimentalStdlibApi::class)
    private val nameMap: Map<ResolvedName, ClassName> = buildMap {
        declarationTree.setupNames(emptyList(), this)
    }

    fun getClassNameForFqName(fqName: ResolvedName): ClassName {
        return nameMap[fqName] ?: error("No name for $fqName")
    }
}