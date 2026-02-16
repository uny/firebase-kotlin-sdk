package dev.ynagai.firebase.firestore

import dev.ynagai.firebase.Timestamp
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.longOrNull
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

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
    is GeoPoint -> geoPointToJsonElement(value)
    is Blob -> blobToJsonElement(value)
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

private fun geoPointToJsonElement(gp: GeoPoint): JsonElement = JsonObject(
    mapOf(
        "latitude" to JsonPrimitive(gp.latitude),
        "longitude" to JsonPrimitive(gp.longitude),
    )
)

@OptIn(ExperimentalEncodingApi::class)
private fun blobToJsonElement(blob: Blob): JsonElement = JsonObject(
    mapOf(
        "_bytes" to JsonPrimitive(Base64.encode(blob.toBytes())),
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
 * Encodes a serializable object into a [Map] suitable for Firestore operations.
 *
 * Uses [kotlinx.serialization] to convert the object to a JSON intermediate representation,
 * then transforms it into a Map<String, Any?>.
 *
 * @param value The object to encode.
 * @param json The [Json] instance to use for serialization.
 * @return A map representation of the object.
 */
inline fun <reified T : Any> encodeToMap(
    value: T,
    json: Json = Json { encodeDefaults = true },
): Map<String, Any?> {
    val jsonElement = json.encodeToJsonElement(value)
    require(jsonElement is JsonObject) { "Expected JsonObject but got ${jsonElement::class.simpleName}" }
    return jsonObjectToMap(jsonElement)
}

@PublishedApi
internal fun jsonObjectToMap(jsonObject: JsonObject): Map<String, Any?> =
    jsonObject.mapValues { (_, v) -> jsonElementToAny(v) }

private fun jsonElementToAny(element: JsonElement): Any? = when (element) {
    is JsonNull -> null
    is JsonPrimitive ->
        if (element.isString) element.content
        else element.booleanOrNull ?: element.longOrNull ?: element.doubleOrNull ?: element.content
    is JsonObject -> jsonObjectToMap(element)
    is JsonArray -> element.map { jsonElementToAny(it) }
}

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
