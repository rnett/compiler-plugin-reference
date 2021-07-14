package test.usage

import tester.second.TestAnnotation
import tester.second.TestEnum

fun main() {
    println("Hello World!")
}

@TestAnnotation(a = [1, 2, 3], e = TestEnum.Three)
fun test() {

}