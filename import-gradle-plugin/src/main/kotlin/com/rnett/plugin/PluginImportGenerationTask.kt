package com.rnett.plugin

import com.rnett.plugin.generator.PluginImportGenerator
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

abstract class PluginImportGenerationTask: DefaultTask() {
    @InputDirectory
    val inputDirectory: DirectoryProperty = project.objects.directoryProperty()

    @Input
    val packageName: Property<String> = project.objects.property(String::class.java)

    @Input
    val className: Property<String> = project.objects.property(String::class.java)

    @OutputDirectory
    val outputDirectory: DirectoryProperty = project.objects.directoryProperty()

    @TaskAction
    fun generate(){
        val input = inputDirectory.get().asFile
        val output = outputDirectory.get().asFile
        val declarations = input.listFiles().orEmpty().flatMap { ExportDeclaration.deserialize(it.readText()) }
        PluginImportGenerator.generate(output, declarations, packageName.get(), className.get())
    }
}