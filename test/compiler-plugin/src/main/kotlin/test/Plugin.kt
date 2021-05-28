package test

import com.google.auto.service.AutoService
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.cli.common.CLIConfigurationKeys
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.com.intellij.mock.MockProject
import org.jetbrains.kotlin.compiler.plugin.AbstractCliOption
import org.jetbrains.kotlin.compiler.plugin.CommandLineProcessor
import org.jetbrains.kotlin.compiler.plugin.ComponentRegistrar
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment

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
        with(pluginContext) {
//            val names = Names(this)
            println()
        }
    }
}