package dev.ynagai.firebase.firestore

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@OptIn(ExperimentalEncodingApi::class)
internal actual fun platformValueToJsonElement(value: Any): JsonElement? = when (value) {
    is com.google.firebase.Timestamp -> JsonObject(
        mapOf(
            "seconds" to JsonPrimitive(value.seconds),
            "nanoseconds" to JsonPrimitive(value.nanoseconds),
        )
    )
    is com.google.firebase.firestore.GeoPoint -> JsonObject(
        mapOf(
            "latitude" to JsonPrimitive(value.latitude),
            "longitude" to JsonPrimitive(value.longitude),
        )
    )
    is com.google.firebase.firestore.Blob -> JsonObject(
        mapOf(
            "_bytes" to JsonPrimitive(Base64.encode(value.toBytes())),
        )
    )
    else -> null
}
