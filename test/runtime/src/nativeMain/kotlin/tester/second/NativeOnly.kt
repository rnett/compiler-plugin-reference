package tester.second

import com.rnett.plugin.export.annotations.PluginExport

actual fun testExpectFun(): String = "test"

actual fun testActualFun(): String = "test"

@PluginExport
fun nativeOnly(arg: Int) {

}