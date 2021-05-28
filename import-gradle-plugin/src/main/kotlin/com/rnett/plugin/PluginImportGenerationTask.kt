package com.rnett.plugin

import com.rnett.plugin.generator.PluginImportGenerator
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

@CacheableTask
abstract class PluginImportGenerationTask : DefaultTask() {
    @InputDirectory
    val inputDirectory: DirectoryProperty = project.objects.directoryProperty()

    @Input
    val packageName: Property<String> = project.objects.property(String::class.java)

    @Input
    val className: Property<String> = project.objects.property(String::class.java)

    @Internal
    val outputDirectory: DirectoryProperty = project.objects.directoryProperty()

    @OutputFile
    private val outputFile: RegularFileProperty = project.objects.fileProperty().value {
        outputDirectory.asFile.get()
            .resolve(packageName.get().replace(".", "/"))
            .resolve("${className.get()}.kt")
    }


    @TaskAction
    fun generate() {
        val input = inputDirectory.get().asFile
        val output = outputDirectory.get().asFile
        val declarations = input.listFiles().orEmpty().flatMap { ExportDeclaration.deserialize(it.readText()) }
        PluginImportGenerator.generate(output, declarations, packageName.get(), className.get())
    }
}