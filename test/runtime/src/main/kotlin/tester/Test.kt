package tester

import com.rnett.plugin.export.annotations.PluginExport

@PluginExport
class TestClass @PluginExport constructor(@PluginExport val n: Int){
    @PluginExport("fromString")
    constructor(s: String): this(s.toInt())
    @PluginExport
    class NestedClass{

    }
}

@PluginExport
typealias TestTypealias = IllegalStateException

@PluginExport
fun testTopLevelFunction(): Int {
    return 2
}

@PluginExport
@PublishedApi
internal fun testPublishedApi(){

}

@PluginExport
@PublishedApi
internal fun testPublishedApi(t: Int){

}