package tester.second

import com.rnett.plugin.export.annotations.PluginExport

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