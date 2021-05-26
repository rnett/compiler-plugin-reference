package com.rnett.plugin

import kotlinx.serialization.Serializable

@Serializable
sealed class ConstantValue {
    abstract val value: Any?

    abstract fun valueAsString(): kotlin.String

    @Serializable
    data class String(override val value: kotlin.String) : ConstantValue() {
        override fun valueAsString(): kotlin.String = "\"$value\""
    }

    @Serializable
    object Null : ConstantValue() {
        override val value: Nothing? = null
        override fun valueAsString(): kotlin.String = "null"
    }

    @Serializable
    data class Boolean(override val value: kotlin.Boolean) : ConstantValue() {
        override fun valueAsString(): kotlin.String = value.toString()
    }

    @Serializable
    data class Char(override val value: kotlin.Char) : ConstantValue() {
        override fun valueAsString(): kotlin.String = "'$value'"
    }

    @Serializable
    data class Byte(override val value: kotlin.Byte) : ConstantValue() {
        override fun valueAsString(): kotlin.String = value.toString()
    }

    @Serializable
    data class Short(override val value: kotlin.Short) : ConstantValue() {
        override fun valueAsString(): kotlin.String = value.toString()
    }

    @Serializable
    data class Int(override val value: kotlin.Int) : ConstantValue() {
        override fun valueAsString(): kotlin.String = value.toString()
    }

    @Serializable
    data class Long(override val value: kotlin.Long) : ConstantValue() {
        override fun valueAsString(): kotlin.String = value.toString()
    }

    @Serializable
    data class Float(override val value: kotlin.Float) : ConstantValue() {
        override fun valueAsString(): kotlin.String = value.toString()
    }

    @Serializable
    data class Double(override val value: kotlin.Double) : ConstantValue() {
        override fun valueAsString(): kotlin.String = value.toString()
    }
}