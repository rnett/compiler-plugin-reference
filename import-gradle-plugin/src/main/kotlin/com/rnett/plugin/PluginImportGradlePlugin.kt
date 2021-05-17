package com.rnett.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilerPluginSupportPlugin
import org.jetbrains.kotlin.gradle.plugin.SubpluginArtifact
import org.jetbrains.kotlin.gradle.plugin.SubpluginOption

class PluginImportGradlePlugin : Plugin<Project> {

    override fun apply(target: Project) {

        val task = target.tasks.create("pluginImport", PluginImportGenerationTask::class.java){
            with(it){
                group = "build"
                className.set("Names")
                target.afterEvaluate {
                    target.tasks.findByName("compileKotlin")!!.dependsOn(this)
                }
            }
        }
        val extension = target.extensions.create("pluginImport", PluginImportExtension::class.java)
        extension.task = task
    }
}