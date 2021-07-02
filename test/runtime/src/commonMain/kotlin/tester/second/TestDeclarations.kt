package tester.second

import com.rnett.plugin.export.annotations.PluginExport
import kotlin.reflect.KClass

//TODO better commonizer tests: expect class w/ additional methods on some platforms
// nesting like class being native only, method on one native

@PluginExport
expect fun testExpectFun(): String

expect fun testActualFun(): String

@PluginExport
annotation class TestAnnotation(
    val t: Int = 2,
    val s: String = testConst,
    val e: TestEnum = TestEnum.Two,
    val c: KClass<*> = TestClass::class,
    val r: ExportedAnnotation = ExportedAnnotation(2),
    val n: NonExported = NonExported("test"),
    val a: IntArray = [1, 2, 3],
)

@PluginExport
annotation class ExportedAnnotation(val i: Int)

annotation class NonExported(val s: String)

@PluginExport
enum class TestEnum {
    One, Two, Three;

    @PluginExport
    val testProp: String = "test"
}

@PluginExport
fun testIsOne(t: TestEnum) = t == TestEnum.One

@PluginExport
const val testConst: String = "test"

@PluginExport
class TestClass @PluginExport constructor(@PluginExport var n: Int) {
    @PluginExport("fromString")
    constructor(s: String) : this(s.toInt())

    @PluginExport
    class NestedClass {

    }
}

@PluginExport
class WithTypeParams<T : Number> @PluginExport constructor(val n: T) {
}

@PluginExport
typealias TestTypealias = IllegalStateException

@PluginExport
typealias TestTypealiasWithArg<T> = List<T>

@PluginExport
fun <T> Int.testTopLevelFunction(req: T, opt: Double? = 2.0, vararg t: String): Int {
    return 2
}

@PluginExport
@PublishedApi
internal fun testPublishedApi() {

}

@PluginExport
@PublishedApi
internal fun testPublishedApi(t: Int) {

}

@PluginExport
val <T : Number> T.testPropWithTypeVar
    get() = toInt()