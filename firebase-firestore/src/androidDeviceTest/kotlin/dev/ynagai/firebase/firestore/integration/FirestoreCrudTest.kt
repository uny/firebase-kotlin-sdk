package dev.ynagai.firebase.firestore.integration

import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class FirestoreCrudTest : FirestoreEmulatorTest() {

    @Test
    fun addAndGetDocument() = runTest {
        val collection = firestore.collection("crud-test")
        val data = mapOf("name" to "Alice", "age" to 30L)

        val docRef = collection.add(data)

        val snapshot = docRef.get()
        assertTrue(snapshot.exists)
        assertEquals("Alice", snapshot.getString("name"))
        assertEquals(30L, snapshot.getLong("age"))
    }

    @Test
    fun setAndGetDocument() = runTest {
        val docRef = firestore.collection("crud-test").document("set-doc")
        val data = mapOf("city" to "Tokyo", "population" to 14000000L)

        docRef.set(data)

        val snapshot = docRef.get()
        assertTrue(snapshot.exists)
        assertEquals("Tokyo", snapshot.getString("city"))
        assertEquals(14000000L, snapshot.getLong("population"))
    }

    @Test
    fun updateDocument() = runTest {
        val docRef = firestore.collection("crud-test").document("update-doc")
        docRef.set(mapOf("name" to "Bob", "age" to 25L))

        docRef.update(mapOf("age" to 26L))

        val snapshot = docRef.get()
        assertEquals("Bob", snapshot.getString("name"))
        assertEquals(26L, snapshot.getLong("age"))
    }

    @Test
    fun deleteDocument() = runTest {
        val docRef = firestore.collection("crud-test").document("delete-doc")
        docRef.set(mapOf("temp" to true))

        docRef.delete()

        val snapshot = docRef.get()
        assertFalse(snapshot.exists)
    }

    @Test
    fun getDataReturnsFullMap() = runTest {
        val docRef = firestore.collection("crud-test").document("data-doc")
        val data = mapOf("key1" to "value1", "key2" to 42L)
        docRef.set(data)

        val snapshot = docRef.get()
        val result = snapshot.getData()

        assertNotNull(result)
        assertEquals("value1", result["key1"])
        assertEquals(42L, result["key2"])
    }

    @Test
    fun getNonExistentDocumentReturnsNotExists() = runTest {
        val docRef = firestore.collection("crud-test").document("does-not-exist")

        val snapshot = docRef.get()

        assertFalse(snapshot.exists)
        assertNull(snapshot.getData())
    }

    @Test
    fun setWithMerge() = runTest {
        val docRef = firestore.collection("crud-test").document("merge-doc")
        docRef.set(mapOf("name" to "Charlie", "age" to 30L))

        docRef.set(mapOf("email" to "charlie@example.com"), merge = true)

        val snapshot = docRef.get()
        assertEquals("Charlie", snapshot.getString("name"))
        assertEquals(30L, snapshot.getLong("age"))
        assertEquals("charlie@example.com", snapshot.getString("email"))
    }

    @Test
    fun documentContainsField() = runTest {
        val docRef = firestore.collection("crud-test").document("contains-doc")
        docRef.set(mapOf("present" to "yes"))

        val snapshot = docRef.get()

        assertTrue(snapshot.contains("present"))
        assertFalse(snapshot.contains("absent"))
    }
}
