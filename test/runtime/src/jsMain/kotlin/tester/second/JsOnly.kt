package tester.second

import com.rnett.plugin.export.annotations.PluginExport

@PluginExport
actual fun testExpectFun(): String = "test"

actual fun testActualFun(): String = "test"

@PluginExport
val jsOnly: Int = 2