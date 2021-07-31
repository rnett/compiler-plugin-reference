package com.rnett.plugin

import kotlinx.serialization.Serializable

@Serializable
data class AnnotationParameter(val index: Int, val kind: AnnotationArgument.Kind, val default: AnnotationArgument?)

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
        data class ExportedEnum(val classFqName: ResolvedName) : Kind()

        @Serializable
        object OpaqueEnum : Kind()

        @Serializable
        object ClassRef : Kind()
    }

    sealed interface Annotation {
        val fqName: ResolvedName
        val arguments: Map<String, AnnotationArgument>
    }

    @Serializable
    data class ExportedAnnotation(
        override val fqName: ResolvedName,
        override val arguments: Map<String, AnnotationArgument>
    ) :
        AnnotationArgument(Kind.ExportedAnnotation(fqName)), Annotation

    @Serializable
    data class OpaqueAnnotation(
        override val fqName: ResolvedName,
        override val arguments: Map<String, AnnotationArgument>
    ) :
        AnnotationArgument(Kind.OpaqueAnnotation(fqName)), Annotation

    @Serializable
    data class Constant(val value: ConstantValue) : AnnotationArgument(Kind.Constant(value.kind))

    @Serializable
    data class Array(val values: List<AnnotationArgument>, val elementKind: Kind) :
        AnnotationArgument(Kind.Array(elementKind)) {
        init {
            require(values.all { elementKind == it.kind }) { "Mismatched array element types" }
        }
    }

    sealed interface Enum {
        val classFqName: ResolvedName
        val name: String
        val ordinal: Int
    }

    @Serializable
    data class ExportedEnum(
        override val classFqName: ResolvedName,
        override val name: String,
        override val ordinal: Int
    ) : AnnotationArgument(Kind.ExportedEnum(classFqName)), Enum

    @Serializable
    data class OpaqueEnum(
        override val classFqName: ResolvedName,
        override val name: String,
        override val ordinal: Int
    ) : AnnotationArgument(Kind.OpaqueEnum), Enum

    @Serializable
    data class ClassRef(val fqName: ResolvedName) : AnnotationArgument(Kind.ClassRef)
}