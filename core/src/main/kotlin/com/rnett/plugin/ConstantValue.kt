package com.rnett.plugin

import kotlinx.serialization.Serializable

@Serializable
sealed class ConstantValue(val kind: Kind) {
    abstract val value: Any?

    abstract fun valueAsString(): kotlin.String

    @Serializable
    sealed class Kind {
        @Serializable
        object String : Kind()

        @Serializable
        object Null : Kind()

        @Serializable
        object Boolean : Kind()

        @Serializable
        object Char : Kind()

        @Serializable
        object Byte : Kind()

        @Serializable
        object Short : Kind()

        @Serializable
        object Int : Kind()

        @Serializable
        object Long : Kind()

        @Serializable
        object Float : Kind()

        @Serializable
        object Double : Kind()
    }

    @Serializable
    data class String(override val value: kotlin.String) : ConstantValue(Kind.String) {
        override fun valueAsString(): kotlin.String = "\"$value\""
    }

    @Serializable
    object Null : ConstantValue(Kind.Null) {
        override val value: Nothing? = null
        override fun valueAsString(): kotlin.String = "null"
    }

    @Serializable
    data class Boolean(override val value: kotlin.Boolean) : ConstantValue(Kind.Boolean) {
        override fun valueAsString(): kotlin.String = value.toString()
    }

    @Serializable
    data class Char(override val value: kotlin.Char) : ConstantValue(Kind.Char) {
        override fun valueAsString(): kotlin.String = "'$value'"
    }

    @Serializable
    data class Byte(override val value: kotlin.Byte) : ConstantValue(Kind.Byte) {
        override fun valueAsString(): kotlin.String = value.toString()
    }

    @Serializable
    data class Short(override val value: kotlin.Short) : ConstantValue(Kind.Short) {
        override fun valueAsString(): kotlin.String = value.toString()
    }

    @Serializable
    data class Int(override val value: kotlin.Int) : ConstantValue(Kind.Int) {
        override fun valueAsString(): kotlin.String = value.toString()
    }

    @Serializable
    data class Long(override val value: kotlin.Long) : ConstantValue(Kind.Long) {
        override fun valueAsString(): kotlin.String = value.toString()
    }

    @Serializable
    data class Float(override val value: kotlin.Float) : ConstantValue(Kind.Float) {
        override fun valueAsString(): kotlin.String = value.toString()
    }

    @Serializable
    data class Double(override val value: kotlin.Double) : ConstantValue(Kind.Double) {
        override fun valueAsString(): kotlin.String = value.toString()
    }
}