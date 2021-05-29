package com.rnett.plugin

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString

enum class PlatformType {
    JVM, JS, Common, Native;
}

@Serializable
data class Platform(val target: String?, val platform: PlatformType, val sourceSets: List<String>) {
    companion object {
        fun serialize(platform: Platform): String {
            try {
                return json.encodeToString(platform)
            } catch (e: Throwable) {
                throw IllegalStateException("Could not serialize exported platform", e)
            }
        }

        fun deserialize(data: String): Platform {
            try {
                return json.decodeFromString(data)
            } catch (e: Throwable) {
                throw IllegalStateException("Could not deserialize exported platform", e)
            }
        }
    }
}