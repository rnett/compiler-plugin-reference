package com.rnett.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

class PluginImportGradlePlugin : Plugin<Project> {

    override fun apply(target: Project) {

        val task = target.tasks.create("pluginImport", PluginImportGenerationTask::class.java) {
            with(it) {
                group = "build"
                className.set("Names")
                target.afterEvaluate {
                    //TODO make multiplatform
                    target.tasks.findByName("compileKotlin")!!.dependsOn(this)
                }
            }
        }
        val extension = target.extensions.create("pluginImport", PluginImportExtension::class.java)
        extension.task = task
    }
}