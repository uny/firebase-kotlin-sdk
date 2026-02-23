package dev.ynagai.firebase.firestore.integration

import dev.ynagai.firebase.firestore.Direction
import dev.ynagai.firebase.firestore.Filter
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class FirestoreQueryTest : FirestoreEmulatorTest() {

    private val collectionPath = "query-test-${System.nanoTime()}"

    @BeforeTest
    fun seedData() = runTest {
        val collection = firestore.collection(collectionPath)
        collection.document("doc1").set(mapOf("name" to "Alice", "age" to 30L, "city" to "Tokyo"))
        collection.document("doc2").set(mapOf("name" to "Bob", "age" to 25L, "city" to "Osaka"))
        collection.document("doc3").set(mapOf("name" to "Charlie", "age" to 35L, "city" to "Tokyo"))
        collection.document("doc4").set(mapOf("name" to "Diana", "age" to 28L, "city" to "Nagoya"))
    }

    @Test
    fun whereEqualToFiltersDocuments() = runTest {
        val result = firestore.collection(collectionPath)
            .whereEqualTo("city", "Tokyo")
            .get()

        assertEquals(2, result.size)
        val names = result.documents.map { it.getString("name") }.toSet()
        assertTrue(names.contains("Alice"))
        assertTrue(names.contains("Charlie"))
    }

    @Test
    fun orderByAscending() = runTest {
        val result = firestore.collection(collectionPath)
            .orderBy("age", Direction.ASCENDING)
            .get()

        assertEquals(4, result.size)
        val names = result.documents.map { it.getString("name") }
        assertEquals(listOf("Bob", "Diana", "Alice", "Charlie"), names)
    }

    @Test
    fun orderByDescending() = runTest {
        val result = firestore.collection(collectionPath)
            .orderBy("age", Direction.DESCENDING)
            .get()

        assertEquals(4, result.size)
        val names = result.documents.map { it.getString("name") }
        assertEquals(listOf("Charlie", "Alice", "Diana", "Bob"), names)
    }

    @Test
    fun limitRestrictsResults() = runTest {
        val result = firestore.collection(collectionPath)
            .orderBy("age", Direction.ASCENDING)
            .limit(2)
            .get()

        assertEquals(2, result.size)
        val names = result.documents.map { it.getString("name") }
        assertEquals(listOf("Bob", "Diana"), names)
    }

    @Test
    fun compositeFilterWithAnd() = runTest {
        val result = firestore.collection(collectionPath)
            .where(
                Filter.and(
                    Filter.equalTo("city", "Tokyo"),
                    Filter.greaterThan("age", 30L),
                )
            )
            .get()

        assertEquals(1, result.size)
        assertEquals("Charlie", result.documents.first().getString("name"))
    }

    @Test
    fun compositeFilterWithOr() = runTest {
        val result = firestore.collection(collectionPath)
            .where(
                Filter.or(
                    Filter.equalTo("city", "Osaka"),
                    Filter.equalTo("city", "Nagoya"),
                )
            )
            .get()

        assertEquals(2, result.size)
        val names = result.documents.map { it.getString("name") }.toSet()
        assertTrue(names.contains("Bob"))
        assertTrue(names.contains("Diana"))
    }

    @Test
    fun whereGreaterThanFilters() = runTest {
        val result = firestore.collection(collectionPath)
            .whereGreaterThan("age", 29L)
            .get()

        assertEquals(2, result.size)
        val names = result.documents.map { it.getString("name") }.toSet()
        assertTrue(names.contains("Alice"))
        assertTrue(names.contains("Charlie"))
    }
}
