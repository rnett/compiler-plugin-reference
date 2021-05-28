package com.rnett.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

class PluginImportGradlePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        val task = target.tasks.register("pluginImport", PluginImportGenerationTask::class.java) {
            it.group = "build"
        }
        target.tasks.named("compileKotlin").configure { it.dependsOn(task) }

        target.extensions.create("pluginImport", PluginImportExtension::class.java, task)
    }
}