package com.rnett.plugin

import com.google.auto.service.AutoService
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.cli.common.CLIConfigurationKeys
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.com.intellij.mock.MockProject
import org.jetbrains.kotlin.compiler.plugin.AbstractCliOption
import org.jetbrains.kotlin.compiler.plugin.CliOption
import org.jetbrains.kotlin.compiler.plugin.CommandLineProcessor
import org.jetbrains.kotlin.compiler.plugin.ComponentRegistrar
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.config.CompilerConfigurationKey
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import org.jetbrains.kotlin.ir.declarations.name
import org.jetbrains.kotlin.ir.util.kotlinFqName
import java.io.File

@AutoService(CommandLineProcessor::class)
class PluginExportCommandLineProcessor : CommandLineProcessor {
    companion object {
        const val OPTION_OUTPUT_DIR = "outputDir"

        val ARG_OUTPUT_DIR = CompilerConfigurationKey<File>(OPTION_OUTPUT_DIR)
    }

    override val pluginId: String = "com.rnett.plugin-export-compiler-plugin"
    override val pluginOptions: Collection<AbstractCliOption> = listOf(
        CliOption(OPTION_OUTPUT_DIR, "output directory", "the output directory", required = true)
    )

    override fun processOption(option: AbstractCliOption, value: String, configuration: CompilerConfiguration) {
        return when (option.optionName) {
            OPTION_OUTPUT_DIR -> configuration.put(ARG_OUTPUT_DIR, File(value))
            else -> throw IllegalArgumentException("Unexpected config option ${option.optionName}")
        }
    }
}

@AutoService(ComponentRegistrar::class)
class PluginExportComponentRegistrar : ComponentRegistrar {
    override fun registerProjectComponents(project: MockProject, configuration: CompilerConfiguration) {
        val outputDir = configuration[PluginExportCommandLineProcessor.ARG_OUTPUT_DIR] ?: error("No option for output dir")
        outputDir.listFiles()?.forEach { it.delete() }
        IrGenerationExtension.registerExtension(
            project,
            PluginExportIrGenerationExtension(
                configuration.get(
                    CLIConfigurationKeys.MESSAGE_COLLECTOR_KEY,
                    MessageCollector.NONE
                ),
                outputDir
            )
        )
    }
}

class PluginExportIrGenerationExtension(val messageCollector: MessageCollector, val outputDir: File) : IrGenerationExtension {
    init {
        outputDir.mkdirs()
    }

    override fun generate(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {
        //TODO use pluginContext.referenceTopLevel()
        moduleFragment.files.forEach {
            val outputFile = outputDir.resolve(it.kotlinFqName.toString() + "_" + it.name.substringBefore('.'))
            val declarations: MutableList<ExportDeclaration> = mutableListOf()
            PluginExporter(pluginContext, messageCollector, declarations::add).visitFile(it)
            outputFile.writeText(ExportDeclaration.serialize(declarations))
        }
    }
}