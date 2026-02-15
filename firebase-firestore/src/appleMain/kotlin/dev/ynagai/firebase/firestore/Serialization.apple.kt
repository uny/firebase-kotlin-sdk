package dev.ynagai.firebase.firestore

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@OptIn(ExperimentalEncodingApi::class)
internal actual fun platformValueToJsonElement(value: Any): JsonElement? {
    nativeTimestampToKmp(value)?.let { ts ->
        return JsonObject(
            mapOf(
                "seconds" to JsonPrimitive(ts.seconds),
                "nanoseconds" to JsonPrimitive(ts.nanoseconds),
            )
        )
    }

    nativeGeoPointToKmp(value)?.let { gp ->
        return JsonObject(
            mapOf(
                "latitude" to JsonPrimitive(gp.latitude),
                "longitude" to JsonPrimitive(gp.longitude),
            )
        )
    }

    nativeBlobToKmp(value)?.let { blob ->
        return JsonObject(
            mapOf(
                "_bytes" to JsonPrimitive(Base64.encode(blob.toBytes())),
            )
        )
    }

    return null
}
