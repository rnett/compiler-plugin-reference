package com.rnett.plugin.export.annotations

@Retention(AnnotationRetention.SOURCE)
@MustBeDocumented
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY, AnnotationTarget.CONSTRUCTOR, AnnotationTarget.TYPEALIAS)
annotation class PluginExport(val name: String = "")