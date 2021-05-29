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
import org.jetbrains.kotlin.platform.isCommon
import org.jetbrains.kotlin.platform.js.isJs
import org.jetbrains.kotlin.platform.jvm.isJvm
import org.jetbrains.kotlin.platform.konan.isNative
import java.io.File

@AutoService(CommandLineProcessor::class)
class PluginExportCommandLineProcessor : CommandLineProcessor {
    companion object {
        const val OPTION_OUTPUT_DIR = "outputDir"
        const val OPTION_TARGET_NAME = "targetName"
        const val OPTION_SOURCE_SETS = "sourceSets"

        val ARG_OUTPUT_DIR = CompilerConfigurationKey<File>(OPTION_OUTPUT_DIR)
        val ARG_TARGET_NAME = CompilerConfigurationKey<String>(OPTION_TARGET_NAME)
        val ARG_SOURCE_SETS = CompilerConfigurationKey<List<String>>(OPTION_SOURCE_SETS)
    }

    override val pluginId: String = "com.rnett.plugin-export-compiler-plugin"
    override val pluginOptions: Collection<AbstractCliOption> = listOf(
        CliOption(OPTION_OUTPUT_DIR, "output directory", "the output directory", required = true),
        CliOption(OPTION_TARGET_NAME, "target name", "the target name", required = false),
        CliOption(OPTION_SOURCE_SETS, "source sets", "the source set names", required = true)
    )

    override fun processOption(option: AbstractCliOption, value: String, configuration: CompilerConfiguration) {
        return when (option.optionName) {
            OPTION_OUTPUT_DIR -> configuration.put(ARG_OUTPUT_DIR, File(value))
            OPTION_TARGET_NAME -> configuration.put(ARG_TARGET_NAME, value)
            OPTION_SOURCE_SETS -> configuration.put(ARG_SOURCE_SETS, value.split("|"))
            else -> throw IllegalArgumentException("Unexpected config option ${option.optionName}")
        }
    }
}

@AutoService(ComponentRegistrar::class)
class PluginExportComponentRegistrar : ComponentRegistrar {
    override fun registerProjectComponents(project: MockProject, configuration: CompilerConfiguration) {
        val outputDir = configuration[PluginExportCommandLineProcessor.ARG_OUTPUT_DIR] ?: error("No option for output dir")
        val targetName = configuration[PluginExportCommandLineProcessor.ARG_TARGET_NAME]
        val sourceSets = configuration[PluginExportCommandLineProcessor.ARG_SOURCE_SETS] ?: error("No option for source sets")

        IrGenerationExtension.registerExtension(
            project,
            PluginExportIrGenerationExtension(
                configuration.get(
                    CLIConfigurationKeys.MESSAGE_COLLECTOR_KEY,
                    MessageCollector.NONE
                ),
                outputDir,
                sourceSets,
                targetName
            )
        )
    }
}

class PluginExportIrGenerationExtension(
    val messageCollector: MessageCollector,
    val outputDir: File,
    val sourceSets: List<String>,
    val targetName: String?
) : IrGenerationExtension {
    init {
        if (outputDir.exists())
            outputDir.listFiles()?.forEach { it.delete() }
    }

    override fun generate(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {
        val declarations = moduleFragment.files.associate {
            val fileName = it.kotlinFqName.toString() + "_" + it.name.substringBefore('.')

            val declarations: MutableList<ExportDeclaration> = mutableListOf()
            PluginExporter(pluginContext, messageCollector, declarations::add).visitFile(it)

            fileName to declarations.toList()
        }.filterValues { it.isNotEmpty() }

        val platformType = when {
            pluginContext.platform.isCommon() -> PlatformType.Common
            pluginContext.platform.isNative() -> PlatformType.Native
            pluginContext.platform.isJs() -> PlatformType.JS
            pluginContext.platform.isJvm() -> PlatformType.JVM
            else -> error("Unknown platforms: ${pluginContext.platform}")
        }

        ExportDeclaration.saveSinglePlatform(outputDir, Platform(targetName, platformType, sourceSets), declarations)
    }
}