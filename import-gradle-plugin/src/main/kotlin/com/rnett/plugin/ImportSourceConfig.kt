package com.rnett.plugin

import com.rnett.plugin.generator.PluginImportGenerator
import org.gradle.api.Project
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.Nested
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinSingleTargetExtension
import java.io.File

private fun Project.pluginExport(targetName: String): File =
    buildDir.resolve("pluginExport").resolve(targetName)

data class ImportConfig(
    @get:Nested val source: ImportSourceConfig,
    @get:Input var packageName: String,
    @get:Input var fileName: String = "Names"
) {
    internal fun outputFile(baseDir: File) =
        baseDir.resolve(packageName.replace(".", "/"))
            .resolve("${fileName}.kt")

    internal fun generate(outputDir: File) {
        if (source.isSinglePlatform) {
            val (platform, declarations) = ExportDeclaration.loadSinglePlatform(source.inputDir)
            PluginImportGenerator.generateSingle(outputDir, platform, declarations, packageName, fileName)
        } else {
            val declarations = ExportDeclaration.loadMultiPlatform(source.inputDir)
            PluginImportGenerator.generateMultiplatform(outputDir, declarations, packageName, fileName)
        }
    }

    fun rename(target: String, new: String, root: Boolean = false) = source.rename(target, new, root)
}

sealed class ImportSourceConfig() {
    @get:Internal
    abstract val source: Project

    @get:Internal
    val dependencyTasks
        get() = dependencyTaskNames.map { source.tasks.named(it) }

    @get:Input
    abstract val dependencyTaskNames: List<String>

    @get:Input
    abstract val isSinglePlatform: Boolean

    @get:InputDirectory
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val inputDir: File

    @get:Input
    protected val renames: MutableMap<String, Pair<String, Boolean>> = mutableMapOf()

    fun rename(target: String, new: String, root: Boolean = false) {
        renames[target] = new to root
    }

    data class SinglePlatform(override val source: Project) : ImportSourceConfig() {
        override val dependencyTaskNames: List<String>
            get() = (source.extensions.findByType(KotlinSingleTargetExtension::class.java)
                ?: error("Project ${source.path} is not a single target Kotlin project, but single platform pluginExport used"))
                .target.compilations.named("main")
                .map { it.compileKotlinTaskName }
                .get()
                .let { listOf(it) }

        override val isSinglePlatform: Boolean = true
        override val inputDir: File = source.pluginExport("")
    }

    data class SingleMultiPlatform(override val source: Project, @get:Input val targetName: String) :
        ImportSourceConfig() {
        override val dependencyTaskNames: List<String>
            get() = (source.extensions.findByType(KotlinMultiplatformExtension::class.java)
                ?: error("Project ${source.path} is not a multiplatform Kotlin project, but multiplatform pluginExport used"))
                .targets.named(targetName).flatMap { it.compilations.named("main") }
                .map { it.compileKotlinTaskName }
                .get()
                .let { listOf(it) }

        override val isSinglePlatform: Boolean = true
        override val inputDir: File = source.pluginExport(targetName)
    }

    data class Multiplatform(override val source: Project) : ImportSourceConfig() {
        override val dependencyTaskNames
            get() =
                (source.extensions.findByType(KotlinMultiplatformExtension::class.java)
                    ?: error("Project ${source.path} is not a multiplatform Kotlin project, but multiplatform pluginExport used"))
                    .targets
                    .map { it.compilations.named("main") }
                    .map { it.get().compileKotlinTaskName }


        override val isSinglePlatform: Boolean = false
        override val inputDir: File = source.buildDir.resolve("pluginExport")

    }
}