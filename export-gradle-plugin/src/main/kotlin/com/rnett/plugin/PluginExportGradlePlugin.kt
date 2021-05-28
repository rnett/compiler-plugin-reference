package com.rnett.plugin

import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilerPluginSupportPlugin
import org.jetbrains.kotlin.gradle.plugin.SubpluginArtifact
import org.jetbrains.kotlin.gradle.plugin.SubpluginOption

class PluginExportGradlePlugin : KotlinCompilerPluginSupportPlugin {

    override fun apply(target: Project) {
        target.extensions.create("pluginExport", PluginExportExtension::class.java)
    }

    override fun applyToCompilation(kotlinCompilation: KotlinCompilation<*>): Provider<List<SubpluginOption>> {
        val target = kotlinCompilation.target
        val project = target.project

        val basePath = project.buildDir.resolve("pluginExport")
        val exportDir = basePath.resolve(target.name).resolve(kotlinCompilation.name)

        project.tasks.getByName(kotlinCompilation.compileKotlinTaskName) {
            it.outputs.dir(exportDir)
                .withPropertyName("pluginExportDir")
        }

        return project.provider {
            listOf(
                SubpluginOption("outputDir", exportDir.absolutePath)
            )
        }
    }

    override fun getCompilerPluginId(): String = "com.rnett.plugin-export-compiler-plugin"

    override fun getPluginArtifact(): SubpluginArtifact = SubpluginArtifact(
        BuildConfig.PROJECT_GROUP_ID,
        BuildConfig.PROJECT_ARTIFACT_ID,
        BuildConfig.PROJECT_VERSION
    )

    override fun isApplicable(kotlinCompilation: KotlinCompilation<*>): Boolean = true
}