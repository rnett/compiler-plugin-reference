package com.rnett.plugin

import org.gradle.api.model.ObjectFactory
import java.io.File

abstract class PluginImportExtension(objects: ObjectFactory) {
    internal lateinit var task: PluginImportGenerationTask

    var packageName
        get() = task.packageName.get()
        set(v) {
            task.packageName.set(v)
        }

    var className
        get() = task.className.get()
        set(v) {
            task.className.set(v)
        }

    var inputDirectory: File
        get() = task.inputDirectory.get().asFile
        set(v) {
            task.inputDirectory.set(v)
        }

    var outputDirectory: File
        get() = task.outputDirectory.get().asFile
        set(v) {
            task.outputDirectory.set(v)
        }

    fun dependsOn(vararg paths: Any) {
        task.dependsOn(paths)
    }

    inline fun task(configure: PluginImportGenerationTask.() -> Unit) {
        `access$task`.apply(configure)
    }

    @PublishedApi
    internal var `access$task`: PluginImportGenerationTask
        get() = task
        set(value) {
            task = value
        }
}