package com.rnett.plugin.generator

import com.rnett.plugin.ExportDeclaration
import com.rnett.plugin.ResolvedName
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals

internal fun Package(name: String, vararg children: DeclarationTree): DeclarationTree.Package =
    DeclarationTree.Package(ResolvedName(name), children.toList())

internal fun Class(name: String): DeclarationTree.Class = DeclarationTree.Class(ExportDeclaration.Class(ResolvedName((name))))


class TestTree {

    @TestFactory
    fun randomTest() = List(100) {
        DynamicTest.dynamicTest("Random test $it") {
            val tree = RandomTree(Random(it), 10)
            val exprs = tree.allDeclarations
            val built = DeclarationTree(exprs)
            assertEquals(tree, built)
        }
    }

    @Test
    fun basicTest1() {
        val declarations = listOf(
            ExportDeclaration.Class(ResolvedName("com.rnett.Class1")),
            ExportDeclaration.Class(ResolvedName("com.rnett.Class2")),
            ExportDeclaration.Class(ResolvedName("com.rnett.Class3")),
            ExportDeclaration.Class(ResolvedName("com.rnett.test.Class1")),
            ExportDeclaration.Class(ResolvedName("com.rnett.test.Class2"))
        )
        val tree = DeclarationTree(declarations)
        assertEquals(
            Package(
                "",
                Package(
                    "com",
                    Package(
                        "com.rnett",
                        Class("com.rnett.Class1"),
                        Class("com.rnett.Class2"),
                        Class("com.rnett.Class3"),
                        Package(
                            "com.rnett.test",
                            Class("com.rnett.test.Class1"),
                            Class("com.rnett.test.Class2"),

                            )
                    )
                )
            ),
            tree
        )
    }
}