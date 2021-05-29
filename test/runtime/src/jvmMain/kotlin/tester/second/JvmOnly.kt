package tester.second

import com.rnett.plugin.export.annotations.PluginExport

actual fun testExpectFun(): String = "test"

@PluginExport
actual fun testActualFun(): String = "test"

@PluginExport
fun jvmOnly(): Int = 2