package com.rnett.plugin.generator

import com.rnett.plugin.ResolvedName
import com.squareup.kotlinpoet.ClassName

internal class FqNameLookup(val declarationTree: DeclarationTree, val rootClass: ClassName) {
    private fun DeclarationTree.setupNames(
        platform: String,
        parent: List<String>,
        map: MutableMap<ResolvedName, ClassName>
    ) {
        val current = parent + displayName
        declaration?.let {
            map[it.referenceFqName] = current.fold(rootClass) { className, it -> className.nestedClass(it) }
        }
        children.filterNot { it is DeclarationTree.PlatformSplit && it.platform.sourceSet != platform }
            .forEach { it.setupNames(platform, current, map) }
    }

    @OptIn(ExperimentalStdlibApi::class)
    private val nameMaps: Map<String, Map<ResolvedName, ClassName>> = declarationTree.allPlatforms.associateWith {
        buildMap {
            declarationTree.setupNames(it, emptyList(), this)
        }
    }

    fun getClassNameForFqName(platform: String, fqName: ResolvedName): ClassName {
        return nameMaps.getValue(platform)[fqName] ?: error("No name for $fqName")
    }
}