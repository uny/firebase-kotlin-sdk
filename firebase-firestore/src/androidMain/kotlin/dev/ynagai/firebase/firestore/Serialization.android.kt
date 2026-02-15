package dev.ynagai.firebase.firestore

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

internal actual fun platformValueToJsonElement(value: Any): JsonElement? = when (value) {
    is com.google.firebase.Timestamp -> JsonObject(
        mapOf(
            "seconds" to JsonPrimitive(value.seconds),
            "nanoseconds" to JsonPrimitive(value.nanoseconds),
        )
    )
    else -> null
}
