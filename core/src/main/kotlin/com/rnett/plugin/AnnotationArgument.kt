package com.rnett.plugin

import kotlinx.serialization.Serializable

@Serializable
sealed class AnnotationArgument(val kind: Kind) {

    @Serializable
    sealed class Kind {
        @Serializable
        data class ExportedAnnotation(val fqName: ResolvedName) : Kind()

        @Serializable
        data class OpaqueAnnotation(val fqName: ResolvedName) : Kind()

        @Serializable
        data class Constant(val valueKind: ConstantValue.Kind) : Kind()

        @Serializable
        data class Array(val elementKind: Kind) : Kind()

        @Serializable
        object Enum : Kind()

        @Serializable
        object ClassRef : Kind()
    }

    @Serializable
    data class ExportedAnnotation(val fqName: ResolvedName, val arguments: Map<String, AnnotationArgument>) :
        AnnotationArgument(Kind.ExportedAnnotation(fqName))

    @Serializable
    data class OpaqueAnnotation(val fqName: ResolvedName, val arguments: Map<String, AnnotationArgument>) :
        AnnotationArgument(Kind.OpaqueAnnotation(fqName))

    @Serializable
    data class Constant(val value: ConstantValue) : AnnotationArgument(Kind.Constant(value.kind))

    @Serializable
    data class Array(val values: List<AnnotationArgument>, val elementKind: Kind) : AnnotationArgument(Kind.Array(elementKind)) {
        init {
            require(values.all { elementKind == it.kind }) { "Mismatched array element types" }
        }
    }

    @Serializable
    data class Enum(val classFqName: ResolvedName, val name: String) : AnnotationArgument(Kind.Enum)

    @Serializable
    data class ClassRef(val fqName: ResolvedName) : AnnotationArgument(Kind.ClassRef)
}