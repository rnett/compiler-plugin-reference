package com.rnett.plugin

import org.jetbrains.kotlin.name.FqName

sealed interface AnnotationArgument

//TODO do I want full opaque argument types?  I probably should, annoying to type though
data class OpaqueAnnotationInstance(val fqName: FqName, val arguments: Map<String, AnnotationArgument>) : AnnotationArgument

data class OpaqueEnumEntry(val classFqName: FqName, val name: String, val ordinal: Int) : AnnotationArgument

data class OpaqueClassRef(val fqName: FqName) : AnnotationArgument

data class Array(val elements: List<AnnotationArgument>) : AnnotationArgument, List<AnnotationArgument> by elements

//TODO types
data class Constant(val value: Any) : AnnotationArgument