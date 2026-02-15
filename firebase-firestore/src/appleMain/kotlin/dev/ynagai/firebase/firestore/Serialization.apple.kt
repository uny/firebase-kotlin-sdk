package dev.ynagai.firebase.firestore

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

internal actual fun platformValueToJsonElement(value: Any): JsonElement? {
    val ts = nativeTimestampToKmp(value) ?: return null
    return JsonObject(
        mapOf(
            "seconds" to JsonPrimitive(ts.seconds),
            "nanoseconds" to JsonPrimitive(ts.nanoseconds),
        )
    )
}
