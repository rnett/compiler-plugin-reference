package com.rnett.plugin

import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.Nested
import org.gradle.api.tasks.OutputFiles
import org.gradle.api.tasks.TaskAction
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import java.io.File

@CacheableTask
abstract class PluginImportGenerationTask : DefaultTask() {

    @Nested
    protected val inputConfigs = mutableListOf<ImportConfig>()

    fun addInputConfig(config: ImportConfig) {
        inputConfigs += config
        val tasks = config.source.dependencyTasks
        dependsOn(*tasks.toTypedArray())
    }

    @Internal
    val outputDirectory: Property<File> = project.objects.property(File::class.java)

    @OutputFiles
    protected val outputFiles: Provider<List<File>> = project.provider {
        val directory = outputDirectory.get()
        inputConfigs.map { it.outputFile(directory) }

    }

    init {
        project.extensions.findByType(KotlinJvmProjectExtension::class.java)?.let {
            outputDirectory.convention(project.provider { project.projectDir.resolve("src/main/kotlin") })
        }
        description = "Import definitions exported from another project as IR accessors"
    }

    @TaskAction
    fun generate() {
        val outputDir = outputDirectory.get()
        inputConfigs.forEach {
            it.generate(outputDir)
        }
    }
}