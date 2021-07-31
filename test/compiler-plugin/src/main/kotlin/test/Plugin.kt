package test

import com.google.auto.service.AutoService
import com.rnett.plugin.get
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.cli.common.CLIConfigurationKeys
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.com.intellij.mock.MockProject
import org.jetbrains.kotlin.compiler.plugin.AbstractCliOption
import org.jetbrains.kotlin.compiler.plugin.CommandLineProcessor
import org.jetbrains.kotlin.compiler.plugin.ComponentRegistrar
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.declarations.IrAnnotationContainer
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import org.jetbrains.kotlin.ir.util.dump
import org.jetbrains.kotlin.ir.visitors.IrElementVisitorVoid
import org.jetbrains.kotlin.ir.visitors.acceptChildrenVoid
import org.jetbrains.kotlin.ir.visitors.acceptVoid
import test.generation.Names.tester.second.TestAnnotation
import test.generation.Names.tester.second.TestEnum
import java.io.File

private val logFile by lazy {
    File("C:\\Users\\jimne\\Google Drive\\My Stuff\\compiler-plugin-reference\\test\\usage\\log.txt").also {
        it.delete()
    }
}

fun log(key: String, data: Any?) {
    logFile.appendText("$key: $data")
}

fun log(str: Any?) {
    logFile.appendText(str.toString())
}

fun log(key: String, ir: IrElement) {
    logFile.appendText("$key:\n${ir.dump(true)}\n\n")
}

@AutoService(CommandLineProcessor::class)
class PluginExportCommandLineProcessor : CommandLineProcessor {
    override val pluginId: String = "test.compiler-plugin"
    override val pluginOptions: Collection<AbstractCliOption> = listOf()
}

@AutoService(ComponentRegistrar::class)
class PluginExportComponentRegistrar : ComponentRegistrar {
    override fun registerProjectComponents(project: MockProject, configuration: CompilerConfiguration) {
        IrGenerationExtension.registerExtension(
            project,
            PluginExportIrGenerationExtension(
                configuration.get(
                    CLIConfigurationKeys.MESSAGE_COLLECTOR_KEY,
                    MessageCollector.NONE
                )
            )
        )
    }
}

class PluginExportIrGenerationExtension(val messageCollector: MessageCollector) : IrGenerationExtension {
    override fun generate(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {
        log("Test")
        with(pluginContext) {
            moduleFragment.acceptVoid(object : IrElementVisitorVoid {
                override fun visitElement(element: IrElement) {
                    if (element is IrAnnotationContainer) {
                        element[TestAnnotation, pluginContext]?.let { instance ->
                            log("Instance", instance)

                            //TODO too hard.  Use sealed class for enum entries, maybe?  Kind/Entries as enum, Instances as sealed that extend DelegatingSymbol/Resolved.  Annotation params as Instances.
                            val s = instance.e is TestEnum.One
                        }
                    }
                    element.acceptChildrenVoid(this)
                }

            })
//            val names = Names(this)
            println()
        }
    }
}