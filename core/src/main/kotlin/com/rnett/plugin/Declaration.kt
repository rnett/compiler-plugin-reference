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

        fun serialize(declarations: Iterable<ExportDeclaration>): String = json.encodeToString(declarations.toList())
        fun deserialize(data: String): List<ExportDeclaration> = json.decodeFromString(data)
    }

    abstract val fqName: ResolvedName
    abstract val signature: Signature
    abstract val customName: String?
    open val defaultName: String get() = fqName.name

    val displayName: String get() = customName ?: defaultName

    val parent: ResolvedName? by lazy { fqName.parent }

    @Serializable
    data class Class(
        override val fqName: ResolvedName,
        override val signature: Signature,
        override val customName: String? = null
    ) : ExportDeclaration()

    @Serializable
    data class Property(
        override val fqName: ResolvedName,
        override val signature: Signature,
        override val customName: String? = null
    ) : ExportDeclaration()

    @Serializable
    data class Function(
        override val fqName: ResolvedName,
        override val signature: Signature,
        override val customName: String? = null,
        val dispatchReceiver: Receiver?,
        val extensionReceivers: List<Receiver>,
        val valueParameters: Map<String, Param>
    ) : ExportDeclaration() {
        @Serializable
        data class Param(val optional: Boolean, val varargs: Boolean)

        @Serializable
        data class Receiver(val typeNic: String)
    }

    @Serializable
    data class Constructor(
        override val fqName: ResolvedName,
        override val signature: Signature,
        override val customName: String? = null
    ) : ExportDeclaration() {
        val classFqName: ResolvedName = fqName.parent!!
        override val defaultName: String
            get() = "ctor"
    }

    @Serializable
    data class Typealias(
        override val fqName: ResolvedName,
        override val signature: Signature,
        override val customName: String? = null
    ) : ExportDeclaration()
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