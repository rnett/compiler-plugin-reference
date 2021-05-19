package com.rnett.plugin.generator

import com.rnett.plugin.ExportDeclaration
import com.rnett.plugin.ResolvedName

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

    override fun toString(): String {
        return fqName.fqName + " : $displayName" + (if (children.isNotEmpty()) "\n" else "") + children.joinToString("\n").prependIndent("    ")
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is DeclarationTree) return false

        if (fqName != other.fqName) return false
        if (children != other.children) return false
        if (declaration != other.declaration) return false

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