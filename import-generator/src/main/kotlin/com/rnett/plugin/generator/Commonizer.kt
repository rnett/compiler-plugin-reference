package com.rnett.plugin.generator

import com.rnett.plugin.Platform
import com.rnett.plugin.ResolvedName
import com.rnett.plugin.SourceSetTree


internal fun Iterable<Platform>.getHighests(): List<ResolvedPlatform> {
    val originalSourceSets = flatMap { it.sourceSets }
        .groupBy({ it.sourceSet }) { it.children }
        .mapValues { it.value.flatten().toSet() }
        .map { it.key to SourceSetTree(it.key, it.value) }
        .toMap()

    val sourceSets = originalSourceSets.toMutableMap()
    originalSourceSets.values.toList().forEach {
        if (it.children.all { name -> name in originalSourceSets }) {
            sourceSets -= it.children
        } else if (it.children.isNotEmpty()) {
            sourceSets -= it.sourceSet
        }
    }

    return sourceSets.keys.map { sourceSet ->
        val sets = filter { it.sourceSets.any { it.sourceSet == sourceSet } }
        ResolvedPlatform(
            sets.mapNotNull { it.target }.toSet(),
            sets.map { it.platform }.toSet(),
            sourceSet
        )
    }
}

internal data class Temp(
    val expect: DeclarationTree,
    val resolvedPlatforms: List<ResolvedPlatform>,
    val actuals: Map<Platform, DeclarationTree>
)

internal fun commonizeChildren(
    parent: ResolvedName,
    currentSourceSet: String,
    platforms: Map<Platform, List<DeclarationTree>>
): List<DeclarationTree> {

    val sameResults = mutableListOf<DeclarationTree>()
    val platformResults = mutableMapOf<ResolvedPlatform, MutableList<DeclarationTree>>()

    platforms.asSequence()
        .flatMap { (key, value) -> value.map { key to it } }
        .groupBy { it.second.withChildren(emptyList()) }
        .asSequence()
        .map { (expect, actuals) ->
            val resolvedPlatforms = actuals.map { it.first }.getHighests()
            Temp(expect, resolvedPlatforms, actuals.toMap())
        }
        .forEach { (base, resolvedPlatforms, declarations) ->
            resolvedPlatforms.forEach {
                val newDeclaration = base.withChildren(
                    commonizeChildren(base.fqName, it.sourceSet, declarations.mapValues { it.value.children })
                )

                if (it.sourceSet == currentSourceSet) {
                    sameResults += newDeclaration
                } else {
                    platformResults.getOrPut(it) { mutableListOf() } += newDeclaration
                }
            }
        }

    return sameResults + platformResults.map {
        DeclarationTree.PlatformSplit(parent, it.key, it.value)
    }

}

fun commonize(roots: Map<Platform, DeclarationTree>): DeclarationTree {
    val fullSourceSets = roots.keys.flatMap { it.sourceSets }
        .groupBy({ it.sourceSet }) { it.children }
        .mapValues { it.value.flatten().toSet() }
        .map { it.key to SourceSetTree(it.key, it.value) }
        .toMap()

    val newRoots =
        roots.mapKeys { it.key.copy(sourceSets = it.key.sourceSets.map { fullSourceSets.getValue(it.sourceSet) }) }

    return DeclarationTree.Package(
        ResolvedName.Root,
        commonizeChildren(
            ResolvedName.Root,
            "commonMain",
            newRoots.filterValues { it.allDeclarations.isNotEmpty() }.mapValues { listOf(it.value) })
    ).removeExtraRoot()
}