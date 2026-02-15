package dev.ynagai.firebase.firestore

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.decodeFromJsonElement

/**
 * Converts a Firestore data map to a [JsonObject] for serialization.
 */
@PublishedApi
internal fun mapToJsonElement(map: Map<String, Any?>): JsonObject =
    JsonObject(map.mapValues { (_, value) -> anyToJsonElement(value) })

private fun anyToJsonElement(value: Any?): JsonElement = when (value) {
    null -> JsonNull
    is Boolean -> JsonPrimitive(value)
    is Number -> JsonPrimitive(value)
    is String -> JsonPrimitive(value)
    is Timestamp -> timestampToJsonElement(value)
    is Map<*, *> -> {
        @Suppress("UNCHECKED_CAST")
        mapToJsonElement(value as Map<String, Any?>)
    }
    is List<*> -> JsonArray(value.map { anyToJsonElement(it) })
    else -> platformValueToJsonElement(value) ?: JsonPrimitive(value.toString())
}

private fun timestampToJsonElement(ts: Timestamp): JsonElement = JsonObject(
    mapOf(
        "seconds" to JsonPrimitive(ts.seconds),
        "nanoseconds" to JsonPrimitive(ts.nanoseconds),
    )
)

/**
 * Platform-specific hook for converting native types (e.g. platform Timestamp)
 * to [JsonElement]. Returns null if the value is not a recognized platform type.
 */
internal expect fun platformValueToJsonElement(value: Any): JsonElement?

/**
 * Deserializes the document data into an object of type [T].
 *
 * Uses [kotlinx.serialization] for deserialization from the document's data map.
 *
 * @return The deserialized object, or null if the document doesn't exist or has no data.
 */
inline fun <reified T : Any> DocumentSnapshot.toObject(json: Json = Json { ignoreUnknownKeys = true }): T? {
    val data = getData() ?: return null
    val jsonElement = mapToJsonElement(data)
    return json.decodeFromJsonElement<T>(jsonElement)
}

/**
 * Deserializes all documents in the query snapshot to a list of type [T].
 *
 * @return A list of deserialized objects.
 */
inline fun <reified T : Any> QuerySnapshot.toObjects(json: Json = Json { ignoreUnknownKeys = true }): List<T> =
    documents.mapNotNull { it.toObject<T>(json) }

/**
 * Gets a field value from the document and casts it to type [T].
 *
 * @param field The name of the field to retrieve.
 * @return The field value cast to [T], or null if the field doesn't exist.
 * @throws IllegalArgumentException if the field exists but cannot be cast to [T].
 */
inline fun <reified T> DocumentSnapshot.getField(field: String): T? {
    val value = get(field) ?: return null
    return value as? T
        ?: throw IllegalArgumentException(
            "Field '$field' is of type ${value::class.simpleName}, cannot be cast to ${T::class.simpleName}"
        )
}
