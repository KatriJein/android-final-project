package com.example.f1application.shared

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.intOrNull

object FinishingPositionSerializer : KSerializer<String> {
    override val descriptor = PrimitiveSerialDescriptor("FinishingPosition", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: String) {
        encoder.encodeString(value)
    }

    override fun deserialize(decoder: Decoder): String {
        if (decoder is JsonDecoder) {
            val jsonElement = decoder.decodeJsonElement()
            if (jsonElement is JsonPrimitive) {
                return jsonElement.intOrNull?.toString() ?: jsonElement.doubleOrNull?.toInt()
                    ?.toString() ?: jsonElement.booleanOrNull?.toString() ?: jsonElement.content
            }
        }
        throw IllegalArgumentException("Expected JsonPrimitive")
    }
}