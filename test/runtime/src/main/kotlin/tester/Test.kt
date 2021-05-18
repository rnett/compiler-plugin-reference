package tester

import com.rnett.plugin.export.annotations.PluginExport

@PluginExport
class TestClass @PluginExport constructor(@PluginExport val n: Int) {
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