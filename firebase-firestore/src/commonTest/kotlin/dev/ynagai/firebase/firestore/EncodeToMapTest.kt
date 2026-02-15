package dev.ynagai.firebase.firestore

import kotlinx.serialization.Serializable
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class EncodeToMapTest {

    @Serializable
    data class SimpleUser(val name: String, val age: Int)

    @Serializable
    data class WithDefaults(val name: String = "default", val count: Int = 0)

    @Serializable
    data class Nested(val label: String, val inner: SimpleUser)

    @Serializable
    data class WithNullable(val name: String, val email: String? = null)

    @Serializable
    data class WithList(val tags: List<String>)

    @Serializable
    data class WithMap(val metadata: Map<String, String>)

    @Test
    fun encodeSimpleObject() {
        val map = encodeToMap(SimpleUser("Alice", 30))
        assertEquals("Alice", map["name"])
        assertEquals(30L, map["age"])
    }

    @Test
    fun encodeWithDefaults() {
        val map = encodeToMap(WithDefaults())
        assertEquals("default", map["name"])
        assertEquals(0L, map["count"])
    }

    @Test
    fun encodeNestedObject() {
        val map = encodeToMap(Nested("outer", SimpleUser("Bob", 25)))
        assertEquals("outer", map["label"])
        @Suppress("UNCHECKED_CAST")
        val inner = map["inner"] as Map<String, Any?>
        assertEquals("Bob", inner["name"])
        assertEquals(25L, inner["age"])
    }

    @Test
    fun encodeWithNullField() {
        val map = encodeToMap(WithNullable("Alice"))
        assertEquals("Alice", map["name"])
        assertNull(map["email"])
    }

    @Test
    fun encodeWithList() {
        val map = encodeToMap(WithList(listOf("kotlin", "firebase")))
        assertEquals(listOf("kotlin", "firebase"), map["tags"])
    }

    @Test
    fun encodeWithMapField() {
        val map = encodeToMap(WithMap(mapOf("key" to "value")))
        @Suppress("UNCHECKED_CAST")
        val metadata = map["metadata"] as Map<String, Any?>
        assertEquals("value", metadata["key"])
    }

    @Test
    fun encodeWithBoolean() {
        @Serializable
        data class WithBool(val active: Boolean)
        val map = encodeToMap(WithBool(true))
        assertEquals(true, map["active"])
    }

    @Test
    fun encodeWithDouble() {
        @Serializable
        data class WithDouble(val score: Double)
        val map = encodeToMap(WithDouble(99.5))
        assertEquals(99.5, map["score"])
    }
}
