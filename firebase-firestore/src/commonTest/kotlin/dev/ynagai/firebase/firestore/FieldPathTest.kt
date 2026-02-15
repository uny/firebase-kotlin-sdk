package dev.ynagai.firebase.firestore

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class FieldPathTest {

    @Test
    fun ofCreatesSingleFieldPath() {
        val path = FieldPath.of("name")
        // Verify it doesn't throw and is constructible
        assertEquals(path, path)
    }

    @Test
    fun ofCreatesNestedFieldPath() {
        val path = FieldPath.of("address", "city")
        assertEquals(path, path)
    }

    @Test
    fun documentIdReturnsFieldPath() {
        val path = FieldPath.documentId()
        assertEquals(path, path)
    }

    @Test
    fun equalFieldPathsAreEqual() {
        val a = FieldPath.of("name")
        val b = FieldPath.of("name")
        assertEquals(a, b)
        assertEquals(a.hashCode(), b.hashCode())
    }

    @Test
    fun differentFieldPathsAreNotEqual() {
        val a = FieldPath.of("name")
        val b = FieldPath.of("age")
        assertNotEquals(a, b)
    }

    @Test
    fun nestedFieldPathEquality() {
        val a = FieldPath.of("address", "city")
        val b = FieldPath.of("address", "city")
        assertEquals(a, b)
        assertEquals(a.hashCode(), b.hashCode())
    }
}
