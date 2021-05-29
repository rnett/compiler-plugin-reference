package com.rnett.plugin.generator

import com.rnett.plugin.ExportDeclaration
import com.rnett.plugin.ResolvedName

fun commonizeChildren(parent: ResolvedName, platforms: Map<String, List<DeclarationTree>>): List<DeclarationTree> {
//    val allHeaders =
    val common = platforms.values.first().mapNotNull { child ->
        val subplatforms = platforms.mapValues { it.value.firstOrNull { it eqNoChildren child } }
            .filterValues { it != null }
            .mapValues { it.value!! }
        if (subplatforms.keys == platforms.keys) {
            val base = subplatforms.values.first()
            base.withChildren(commonizeChildren(base.fqName, subplatforms.mapValues { it.value.children }))
        } else
            null
    }

    val subplatforms = platforms
        .map {
            val children = it.value.filter { child ->
                common.none { it eqNoChildren child }
            }

            DeclarationTree.PlatformSplit(parent, it.key, children)
        }
        .filter { it.children.isNotEmpty() }

    return common + subplatforms

}

fun commonize(roots: Map<String, DeclarationTree>) = DeclarationTree.Package(
    ResolvedName.Root,
    commonizeChildren(ResolvedName.Root, roots.filterValues { it.allDeclarations.isNotEmpty() }.mapValues { listOf(it.value) })
).removeExtraRoot()

sealed class DeclarationTree(
    val fqName: ResolvedName,
    children: List<DeclarationTree>,
    val displayName: String
) {
    val children = children.sortedBy { it.fqName.name }
    abstract val declaration: ExportDeclaration?

    abstract fun withName(name: String): DeclarationTree
    abstract fun withChildren(children: List<DeclarationTree>): DeclarationTree

    val allDeclarations: List<ExportDeclaration> get() = listOfNotNull(declaration) + children.flatMap { it.allDeclarations }

    val allPlatforms: Set<String> get() = setOfNotNull(if (this is PlatformSplit) platform else null) + children.flatMap { it.allPlatforms }

    override fun toString(): String {
        return fqName.fqName + " : $displayName" + (if (children.isNotEmpty()) "\n" else "") + children.joinToString("\n").prependIndent("    ")
    }

    infix fun eqNoChildren(other: DeclarationTree): Boolean =
        fqName == other.fqName && declaration == other.declaration && this::class == other::class

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is DeclarationTree) return false

        if (fqName != other.fqName) return false
        if (children != other.children) return false
        if (declaration != other.declaration) return false
        if (this::class != other::class) return false

        return true
    }

    override fun hashCode(): Int {
        var result = fqName.hashCode()
        result = 31 * result + children.hashCode()
        result = 31 * result + declaration.hashCode()
        return result
    }

    class Package(fqName: ResolvedName, children: List<DeclarationTree>, displayName: String = fqName.name) :
        DeclarationTree(fqName, children, displayName) {
        val isRoot get() = fqName.isRoot
        override val declaration: ExportDeclaration? = null

        override fun withName(name: String) = Package(fqName, children, name)
        override fun withChildren(children: List<DeclarationTree>) = Package(fqName, children, displayName)

        override fun toString(): String {
            if (isRoot) {
                return children.joinToString("\n")
            }
            return super.toString()
        }
    }

    class Class(
        override val declaration: ExportDeclaration.Class,
        children: List<DeclarationTree> = emptyList(),
        displayName: String = declaration.displayName
    ) : DeclarationTree(declaration.fqName, children, displayName) {

        override fun withName(name: String) = Class(declaration, children, name)
        override fun withChildren(children: List<DeclarationTree>) = Class(declaration, children, displayName)
    }

    class Leaf(
        override val declaration: ExportDeclaration,
        displayName: String = declaration.displayName
    ) : DeclarationTree(declaration.fqName, emptyList(), displayName) {

        override fun withName(name: String) = Leaf(declaration, name)
        override fun withChildren(children: List<DeclarationTree>) = this
    }

    class PlatformSplit(parentFqName: ResolvedName, val platform: String, children: List<DeclarationTree>) :
        DeclarationTree(parentFqName.child(platform), children, platform) {
        override val declaration: ExportDeclaration? = null

        override fun withName(name: String): DeclarationTree = error("Can't rename platforms, and should never have to")

        override fun withChildren(children: List<DeclarationTree>): DeclarationTree = PlatformSplit(fqName.parent!!, fqName.name, children)
    }

    private fun collapsePackages(): DeclarationTree {
        if (children.size == 1) {
            val child = children[0]
            if (child is Class || child is Package)
                return child.collapsePackages()
        }
        return this
    }

    private fun disambiguateOverloads(): DeclarationTree {
        val nameUsages = mutableMapOf<String, Int>()
        return withChildren(children.map {
            val count = nameUsages.getOrElse(it.displayName) { 0 }
            nameUsages[it.displayName] = count + 1

            if (count == 0)
                it
            else {
                it.withName(it.displayName + "_$count")
            }
        }.map { it.disambiguateOverloads() }
        )

    }

    fun finalize() = this.disambiguateOverloads()

    fun sort() = withChildren(children.sortedBy { it.fqName.fqName })

    fun removeExtraRoot() = if (this is Package && this.isRoot && this.children.size == 1)
        children[0]
    else
        this

    fun ensureRoot() = if (this is Package && this.isRoot)
        this
    else
        Package(ResolvedName.Root, listOf(this))

    companion object {
        operator fun invoke(declarations: Iterable<ExportDeclaration>): DeclarationTree {
            return buildNode(0, "", null, declarations).finalize()
        }

        private fun buildNode(
            index: Int,
            prefix: String,
            topClass: ExportDeclaration.Class?,
            declarations: Iterable<ExportDeclaration>
        ): DeclarationTree {
            val leaves = declarations.filter { it.fqName.parts.size - 1 == index }.map {
                if (it is ExportDeclaration.Class)
                    Class(it)
                else
                    Leaf(it)
            }.toMutableList()

            val subTrees = declarations.filter { it.fqName.parts.size - 1 > index }.groupBy { it.fqName.parts[index] }.mapKeys {
                if (prefix.isNotBlank())
                    "$prefix.${it.key}"
                else
                    it.key
            }.map {
                val name = it.key
                val topClass = leaves.filterIsInstance<Class>().firstOrNull { it.fqName.fqName == name }
                topClass?.let { leaves.remove(it) }
                buildNode(index + 1, name, topClass?.declaration, it.value)
            }

            return if (topClass == null) {
                Package(ResolvedName(prefix), subTrees + leaves)
            } else {
                Class(topClass, subTrees + leaves)
            }
        }

    }
}