package com.rnett.plugin

import com.rnett.plugin.generator.PluginImportGenerator
import org.gradle.api.Project
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.Nested
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinSingleTargetExtension
import java.io.File

private fun Project.pluginExport(targetName: String): File =
    buildDir.resolve("pluginExport").resolve(targetName)

data class ImportConfig(@Nested val source: ImportSourceConfig, @Input var packageName: String, @Input var fileName: String = "Names") {
    internal fun outputFile(baseDir: File) =
        baseDir.resolve(packageName.replace(".", "/"))
            .resolve("${fileName}.kt")

    internal fun generate(outputDir: File) {
        if (source.isSinglePlatform) {
            val declarations = source.inputDir.listFiles().orEmpty().flatMap { ExportDeclaration.deserialize(it.readText()) }
            PluginImportGenerator.generateSingle(outputDir, declarations, packageName, fileName)
        } else {
            val declarations = source.inputDir.listFiles { it: File -> it.isDirectory }.orEmpty().associate {
                it.name to it.listFiles().orEmpty().flatMap { ExportDeclaration.deserialize(it.readText()) }
            }
            PluginImportGenerator.generateMultiplatform(outputDir, declarations, packageName, fileName)
        }
    }

    fun rename(target: String, new: String, root: Boolean = false) = source.rename(target, new, root)
}

sealed class ImportSourceConfig(@Input val source: Project) {

    @get:Input
    val dependencyTasks
        get() = dependencyTaskNames.map { source.tasks.named(it) }

    @get:Input
    abstract val dependencyTaskNames: List<String>

    @get:Input
    abstract val isSinglePlatform: Boolean

    @get:InputDirectory
    abstract val inputDir: File

    @Input
    protected val renames: MutableMap<String, Pair<String, Boolean>> = mutableMapOf()

    fun rename(target: String, new: String, root: Boolean = false) {
        renames[target] = new to root
    }

    class SinglePlatform(source: Project) : ImportSourceConfig(source) {
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

    class SingleMultiPlatform(source: Project, @Internal val targetName: String) : ImportSourceConfig(source) {
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

    class Multiplatform(source: Project) : ImportSourceConfig(source) {
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