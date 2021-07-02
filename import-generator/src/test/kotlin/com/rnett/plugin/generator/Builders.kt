package com.rnett.plugin.generator

import com.rnett.plugin.ExportDeclaration
import com.rnett.plugin.ResolvedName
import com.rnett.plugin.Signature
import com.rnett.plugin.TypeString
import kotlin.random.Random

class NameHelper {
    private var packages = 0
    private var classes = 0
    private var properties = 0
    private var functions = 0
    private var constructors = 0
    private var typealises = 0

    fun packageName() = "package${++packages}"
    fun className() = "Class${++classes}"
    fun propertyName() = "prop${++properties}"
    fun functionName() = "func${++functions}"
    fun constructorName() = "<init${++constructors}>"
    fun typealiasName() = "TypeAlias${++typealises}"
}

fun RandomTree(random: Random, depth: Int) = DeclarationTree {
    RandomTreeHelper(random, depth, NameHelper())
}

private fun NamespaceBuilder.RandomTreeHelper(random: Random, depth: Int, names: NameHelper) {
    repeat(random.nextInt(10)) {
        when (random.nextInt(6)) {
            0 -> if (this is PackageBuilder) {
                Package(names.packageName()) { RandomTreeHelper(random, depth - 1, names) }
            }
            1 -> Class(names.className()) { RandomTreeHelper(random, depth - 1, names) }
            2 -> Property(names.propertyName())
            3 -> Function(names.functionName())
            4 -> if (this is ClassBuilder) Constructor(names.constructorName())
            5 -> Typealias(names.typealiasName())
        }
    }
}

inline fun DeclarationTree(builder: PackageBuilder.() -> Unit): DeclarationTree =
    PackageBuilder(ResolvedName.Root).apply(builder).build().finalize()

interface NamespaceBuilder {

    fun child(child: DeclarationTree)

    fun childName(name: String): ResolvedName

    fun Property(name: String) =
        Declaration(
            ExportDeclaration.Property(
                childName(name),
                Signature.None,
                TypeString.None,
                null,
                emptyList(),
                emptyList(),
                false,
                false,
                false,
                null
            )
        )

    fun Function(name: String) =
        Declaration(
            ExportDeclaration.Function(
                childName(name),
                Signature.None,
                TypeString.None,
                null,
                emptyList(),
                emptyList(),
                emptyList()
            )
        )

    fun Typealias(name: String) = Declaration(ExportDeclaration.Typealias(childName(name), Signature.None, emptyList()))
}

fun NamespaceBuilder.Declaration(declaration: ExportDeclaration) {
    child(DeclarationTree.Leaf(declaration))
}

inline fun PackageBuilder.Package(name: String, body: PackageBuilder.() -> Unit) {
    val built = PackageBuilder(childName(name)).apply(body).build()
    if (built.children.isNotEmpty()) {
        child(built)
    }
}

inline fun PlatformBuilder.Package(name: String, body: PackageBuilder.() -> Unit) {
    val built = PackageBuilder(childName(name)).apply(body).build()
    if (built.children.isNotEmpty()) {
        child(built)
    }
}

inline fun NamespaceBuilder.Class(name: String, body: ClassBuilder.() -> Unit = {}) {
    child(ClassBuilder(childName(name)).apply(body).build())
}

inline fun NamespaceBuilder.Platform(name: String, body: PlatformBuilder.() -> Unit = {}) {
    child(PlatformBuilder(childName(name)).apply(body).build())
}

class PackageBuilder(val fqName: ResolvedName) : NamespaceBuilder {
    private val declarations: MutableList<DeclarationTree> = mutableListOf()
    override fun child(child: DeclarationTree) {
        declarations += child
    }

    override fun childName(name: String): ResolvedName = fqName.child(name)

    fun build() = DeclarationTree.Package(fqName, declarations)
}

class ClassBuilder(val fqName: ResolvedName) : NamespaceBuilder {
    private val declarations: MutableList<DeclarationTree> = mutableListOf()
    override fun child(child: DeclarationTree) {
        declarations += child
    }

    override fun childName(name: String): ResolvedName = fqName.child(name)

    fun build() =
        DeclarationTree.Class(ExportDeclaration.Class(fqName, Signature.None, listOf(), null, null), declarations)

    fun Constructor(name: String = "<init>") =
        Declaration(
            ExportDeclaration.Constructor(
                childName(name),
                Signature.None,
                TypeString.None,
                emptyList(),
                emptyList()
            )
        )
}

class PlatformBuilder(val fqName: ResolvedName) : NamespaceBuilder {
    private val declarations: MutableList<DeclarationTree> = mutableListOf()
    override fun child(child: DeclarationTree) {
        declarations += child
    }

    override fun childName(name: String): ResolvedName = fqName.parent!!.child(name)

    fun build() =
        DeclarationTree.PlatformSplit(fqName.parent!!, ResolvedPlatform(setOf(), setOf(), fqName.fqName), declarations)

    fun Constructor(name: String = "<init>") =
        Declaration(
            ExportDeclaration.Constructor(
                childName(name),
                Signature.None,
                TypeString.None,
                emptyList(),
                emptyList()
            )
        )
}

