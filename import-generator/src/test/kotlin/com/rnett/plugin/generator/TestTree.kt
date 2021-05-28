package com.rnett.plugin.generator

import com.rnett.plugin.ExportDeclaration
import com.rnett.plugin.ResolvedName
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals

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
}