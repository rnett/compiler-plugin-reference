package com.rnett.plugin

import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.jetbrains.kotlin.gradle.dsl.KotlinSingleTargetExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilerPluginSupportPlugin
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.plugin.SubpluginArtifact
import org.jetbrains.kotlin.gradle.plugin.SubpluginOption

fun KotlinSourceSet.allDeps(): Set<KotlinSourceSet> = dependsOn + dependsOn.flatMap { it.allDeps() }.toSet()

class PluginExportGradlePlugin : KotlinCompilerPluginSupportPlugin {

    override fun apply(target: Project) {
        target.extensions.create("pluginExport", PluginExportExtension::class.java)
    }

    override fun applyToCompilation(kotlinCompilation: KotlinCompilation<*>): Provider<List<SubpluginOption>> {
        val target = kotlinCompilation.target
        val project = target.project

        val basePath = project.buildDir.resolve("pluginExport")


        val targetName = if (project.extensions.findByType(KotlinSingleTargetExtension::class.java) != null) {
            null
        } else {
            target.name
        }

        val exportDir = targetName?.let { basePath.resolve(it) } ?: basePath

        project.tasks.named(kotlinCompilation.compileKotlinTaskName).configure {
            it.outputs.dir(exportDir)
                .withPropertyName("pluginExportDirOutput")
        }

        val sourceSets = kotlinCompilation.allKotlinSourceSets

        val sourceSetStr = sourceSets.joinToString("||") { set ->
            val children = sourceSets.filter { set in it.allDeps() }
            if (children.isNotEmpty())
                set.name + "|" + children.joinToString("|") { it.name }
            else
                set.name
        }

        return project.provider {
            listOfNotNull(
                SubpluginOption("outputDir", exportDir.absolutePath),
                targetName?.let { SubpluginOption("targetName", it) },
                SubpluginOption("sourceSets", sourceSetStr),
            )
        }
    }

    override fun getCompilerPluginId(): String = "com.rnett.plugin-export-compiler-plugin"

    override fun getPluginArtifact(): SubpluginArtifact = SubpluginArtifact(
        BuildConfig.PROJECT_GROUP_ID,
        BuildConfig.PROJECT_ARTIFACT_ID,
        BuildConfig.PROJECT_VERSION
    )

    override fun getPluginArtifactForNative(): SubpluginArtifact = SubpluginArtifact(
        BuildConfig.PROJECT_GROUP_ID,
        BuildConfig.PROJECT_ARTIFACT_ID + "-native",
        BuildConfig.PROJECT_VERSION
    )


    override fun isApplicable(kotlinCompilation: KotlinCompilation<*>): Boolean =
        kotlinCompilation.compilationName == "main"
}