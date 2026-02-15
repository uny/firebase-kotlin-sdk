package dev.ynagai.firebase.firestore

import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class SerializationTest {

    @Test
    fun mapToJsonElementWithStrings() {
        val map = mapOf("name" to "Alice", "city" to "Tokyo")
        val json = mapToJsonElement(map)
        assertEquals("Alice", json["name"]?.jsonPrimitive?.content)
        assertEquals("Tokyo", json["city"]?.jsonPrimitive?.content)
    }

    @Test
    fun mapToJsonElementWithNumbers() {
        val map = mapOf<String, Any?>("age" to 30, "score" to 99.5)
        val json = mapToJsonElement(map)
        assertEquals("30", json["age"]?.jsonPrimitive?.content)
        assertEquals("99.5", json["score"]?.jsonPrimitive?.content)
    }

    @Test
    fun mapToJsonElementWithBoolean() {
        val map = mapOf<String, Any?>("active" to true, "deleted" to false)
        val json = mapToJsonElement(map)
        assertEquals("true", json["active"]?.jsonPrimitive?.content)
        assertEquals("false", json["deleted"]?.jsonPrimitive?.content)
    }

    @Test
    fun mapToJsonElementWithNull() {
        val map = mapOf<String, Any?>("value" to null)
        val json = mapToJsonElement(map)
        assertEquals(JsonNull, json["value"])
    }

    @Test
    fun mapToJsonElementWithNestedMap() {
        val map = mapOf<String, Any?>(
            "address" to mapOf("city" to "Tokyo", "zip" to "100-0001")
        )
        val json = mapToJsonElement(map)
        val address = json["address"]?.jsonObject
        assertEquals("Tokyo", address?.get("city")?.jsonPrimitive?.content)
        assertEquals("100-0001", address?.get("zip")?.jsonPrimitive?.content)
    }

    @Test
    fun mapToJsonElementWithList() {
        val map = mapOf<String, Any?>(
            "tags" to listOf("kotlin", "firebase")
        )
        val json = mapToJsonElement(map)
        val tags = json["tags"]?.jsonArray
        assertEquals(2, tags?.size)
        assertEquals("kotlin", tags?.get(0)?.jsonPrimitive?.content)
        assertEquals("firebase", tags?.get(1)?.jsonPrimitive?.content)
    }

    @Test
    fun mapToJsonElementWithEmptyMap() {
        val map = emptyMap<String, Any?>()
        val json = mapToJsonElement(map)
        assertTrue(json.isEmpty())
    }

    @Test
    fun mapToJsonElementWithTimestamp() {
        val map = mapOf<String, Any?>(
            "createdAt" to Timestamp(seconds = 1707994800, nanoseconds = 123456789)
        )
        val json = mapToJsonElement(map)
        val ts = json["createdAt"]?.jsonObject
        assertNotNull(ts)
        assertEquals("1707994800", ts["seconds"]?.jsonPrimitive?.content)
        assertEquals("123456789", ts["nanoseconds"]?.jsonPrimitive?.content)
    }

    @Test
    fun mapToJsonElementWithMixedTypes() {
        val map = mapOf<String, Any?>(
            "name" to "Bob",
            "age" to 25,
            "active" to true,
            "email" to null,
            "scores" to listOf(90, 85, 95),
            "address" to mapOf("city" to "Osaka"),
        )
        val json = mapToJsonElement(map)
        assertEquals(6, json.size)
        assertEquals(JsonPrimitive("Bob"), json["name"])
        assertEquals(JsonNull, json["email"])
    }
}
