package com.rnett.plugin

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
sealed class ExportDeclaration {
    companion object {
        private val json: Json = Json {

        }

        fun serialize(declarations: Iterable<ExportDeclaration>): String {
            try {
                return json.encodeToString(declarations.toList())
            } catch (e: Throwable) {
                throw IllegalStateException("Could not serialize exported declarations", e)
            }
        }

        fun deserialize(data: String): List<ExportDeclaration> {
            try {
                return json.decodeFromString(data)
            } catch (e: Throwable) {
                throw IllegalStateException("Could not deserialize exported declarations", e)
            }
        }
    }

    abstract val fqName: ResolvedName
    open val referenceFqName: ResolvedName get() = fqName
    abstract val signature: Signature
    abstract val customName: String?
    open val defaultName: String get() = fqName.name

    val displayName: String get() = customName ?: defaultName

    val parent: ResolvedName? by lazy { fqName.parent }

    @Serializable
    enum class Variance {
        IN, OUT, NONE;

        val render get() = if (this == NONE) "" else name.lowercase()
        val prefix
            get() = render.let {
                if (it.isNotBlank())
                    " $it"
                else
                    ""
            }

    }

    @Serializable
    data class TypeParameter(val name: String, val index: Int, val variance: Variance, val supertypes: List<TypeString>)

    @Serializable
    data class Param(val name: String, val index: Int, val optional: Boolean, val varargs: Boolean, val type: TypeString)

    @Serializable
    data class Receiver(val typeNic: String, val type: TypeString)

    @Serializable
    data class Class(
        override val fqName: ResolvedName,
        override val signature: Signature,
        val typeParameters: List<TypeParameter>,
        val enumNames: List<String>?,
        val annotationProperties: Map<String, AnnotationArgument?>?,
        override val customName: String? = null
    ) : ExportDeclaration()

    @Serializable
    data class Property(
        override val fqName: ResolvedName,
        override val signature: Signature,
        val valueType: TypeString,
        val dispatchReceiver: Receiver?,
        val extensionReceivers: List<Receiver>,
        val typeParameters: List<TypeParameter>,
        val hasGetter: Boolean,
        val hasSetter: Boolean,
        val hasField: Boolean,
        val constantValue: ConstantValue?,
        override val customName: String? = null
    ) : ExportDeclaration()

    @Serializable
    data class Function(
        override val fqName: ResolvedName,
        override val signature: Signature,
        val returnType: TypeString,
        val dispatchReceiver: Receiver?,
        val extensionReceivers: List<Receiver>,
        val typeParameters: List<TypeParameter>,
        val valueParameters: List<Param>,
        override val customName: String? = null,
    ) : ExportDeclaration()

    @Serializable
    data class Constructor(
        override val fqName: ResolvedName,
        override val signature: Signature,
        val constructedClass: TypeString,
        val classTypeParams: List<TypeParameter>,
        val valueParameters: List<Param>,
        override val customName: String? = null
    ) : ExportDeclaration() {
        val classFqName: ResolvedName = fqName.parent!!
        override val referenceFqName: ResolvedName = classFqName
        override val defaultName: String
            get() = "ctor"
    }

    @Serializable
    data class Typealias(
        override val fqName: ResolvedName,
        override val signature: Signature,
        val typeParameters: List<TypeParameter>,
        val expandedType: TypeString,
        override val customName: String? = null
    ) : ExportDeclaration()
}

@Serializable
@JvmInline
value class TypeString(val type: String) {
    override fun toString(): String = type
    val kdoc get() = "`$type`"

    companion object {
        val None = TypeString("")
    }
}

@Serializable
data class Signature(val packageFqName: String, val declarationFqName: String, val id: Long?, val mask: Long) {
    companion object {
        val None = Signature("", "", null, 0)
    }
}

@Serializable
@JvmInline
value class ResolvedName(val fqName: String) {
    val parent: ResolvedName? get() = fqName.substringBeforeLast(".", "").ifBlank { null }?.let(::ResolvedName)
    val parts: List<String> get() = fqName.split(".")
    val name: String get() = fqName.substringAfterLast('.')

    val isRoot get() = fqName.isBlank()

    fun child(name: String): ResolvedName = if (isRoot)
        ResolvedName(name)
    else
        ResolvedName(fqName.trimEnd('.') + "." + name.trimStart('.'))

    companion object {
        val Root = ResolvedName("")
    }
}