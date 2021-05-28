package com.rnett.plugin

import org.gradle.api.Project
import org.gradle.api.tasks.TaskProvider
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinSingleTargetExtension
import java.io.File

abstract class PluginImportExtension(val task: TaskProvider<PluginImportGenerationTask>) {
    fun outputDirectory(file: File) {
        task.configure { it.outputDirectory.set(file) }
    }

    fun dependsOn(vararg paths: Any) {
        task.configure { dependsOn(paths) }
    }

    fun import(project: Project, packageName: String, fileName: String = "Names", builder: ImportConfig.() -> Unit = {}) {
        task.configure {
            val source = when {
                project.extensions.findByType(KotlinSingleTargetExtension::class.java) != null -> {
                    ImportSourceConfig.SinglePlatform(project)
                }
                project.extensions.findByType(KotlinMultiplatformExtension::class.java) != null -> {
                    ImportSourceConfig.Multiplatform(project)
                }
                else -> {
                    error("Project ${project.path} must be a single platform or multiplatform Kotlin project.")
                }
            }

            it.addInputConfig(ImportConfig(source, packageName, fileName).apply(builder))
        }
    }

    fun importSingle(project: Project, targetName: String, packageName: String, fileName: String = "Names", builder: ImportConfig.() -> Unit = {}) {
        task.configure {
            val source = when {
                project.extensions.findByType(KotlinSingleTargetExtension::class.java) != null -> {
                    ImportSourceConfig.SinglePlatform(project)
                }
                project.extensions.findByType(KotlinMultiplatformExtension::class.java) != null -> {
                    ImportSourceConfig.SingleMultiPlatform(project, targetName)
                }
                else -> {
                    error("Project ${project.path} must be a single platform or multiplatform Kotlin project.")
                }
            }

            it.addInputConfig(ImportConfig(source, packageName, fileName).apply(builder))
        }
    }

    inline fun task(crossinline configure: PluginImportGenerationTask.() -> Unit) {
        task.configure { configure(it) }
    }
}