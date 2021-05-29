package com.rnett.plugin.generator

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

    @TestFactory
    fun randomCommonizerTest() = List(10) {
        DynamicTest.dynamicTest("Random commonizer test $it") {
            val tree = RandomTree(Random(it), 3)
            val platforms = mapOf("jvm" to tree, "js" to tree, "native" to tree)
            val common = commonize(platforms)

            assertEquals(tree, common)
        }
    }

    @Test
    fun commonizerTest() {
        val jvm = DeclarationTree {
            Package("test") {
                Class("CommonClass1")
                Class("CommonClass2") {
                    Function("JvmFunction")
                    Function("CommonFunction")
                }
                Class("JvmClass")
                Class("JvmNativeClass")
            }
        }
        val js = DeclarationTree {
            Package("test") {
                Class("CommonClass1")
                Class("CommonClass2") {
                    Function("CommonFunction")
                    Property("JsProperty")
                }
            }
        }
        val native = DeclarationTree {
            Package("test") {
                Class("CommonClass1")
                Class("CommonClass2") {
                    Function("CommonFunction")
                }
                Class("JvmNativeClass")
            }
        }

        val expected = DeclarationTree {
            Package("test") {
                Class("CommonClass1")
                Class("CommonClass2") {
                    Platform("jvm") {
                        Function("JvmFunction")
                    }
                    Function("CommonFunction")
                    Platform("js") {
                        Property("JsProperty")
                    }
                }
                Platform("jvm") {
                    Class("JvmClass")
                    Class("JvmNativeClass")
                }
                Platform("native") {
                    Class("JvmNativeClass")
                }
            }
        }

        val commonized = commonize(mapOf("jvm" to jvm, "js" to js, "native" to native))

        assertEquals(expected.sort(), commonized.sort())
    }
}