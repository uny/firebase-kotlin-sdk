package dev.ynagai.firebase.firestore.integration

import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class FirestoreWriteBatchTest : FirestoreEmulatorTest() {

    @Test
    fun batchSetMultipleDocuments() = runTest {
        val collection = firestore.collection("batch-test")
        val doc1 = collection.document("batch-doc1")
        val doc2 = collection.document("batch-doc2")

        firestore.batch()
            .set(doc1, mapOf("name" to "Frank"))
            .set(doc2, mapOf("name" to "Grace"))
            .commit()

        val snap1 = doc1.get()
        val snap2 = doc2.get()
        assertTrue(snap1.exists)
        assertTrue(snap2.exists)
        assertEquals("Frank", snap1.getString("name"))
        assertEquals("Grace", snap2.getString("name"))
    }

    @Test
    fun batchUpdateDocument() = runTest {
        val docRef = firestore.collection("batch-test").document("batch-update")
        docRef.set(mapOf("name" to "Hank", "age" to 40L))

        firestore.batch()
            .update(docRef, mapOf("age" to 41L))
            .commit()

        val snapshot = docRef.get()
        assertEquals("Hank", snapshot.getString("name"))
        assertEquals(41L, snapshot.getLong("age"))
    }

    @Test
    fun batchDeleteDocument() = runTest {
        val docRef = firestore.collection("batch-test").document("batch-delete")
        docRef.set(mapOf("temp" to true))

        firestore.batch()
            .delete(docRef)
            .commit()

        val snapshot = docRef.get()
        assertFalse(snapshot.exists)
    }

    @Test
    fun batchMixedOperations() = runTest {
        val collection = firestore.collection("batch-test")
        val docSet = collection.document("batch-mixed-set")
        val docUpdate = collection.document("batch-mixed-update")
        val docDelete = collection.document("batch-mixed-delete")

        // Prepare existing docs
        docUpdate.set(mapOf("status" to "old"))
        docDelete.set(mapOf("temp" to true))

        firestore.batch()
            .set(docSet, mapOf("status" to "new"))
            .update(docUpdate, mapOf("status" to "updated"))
            .delete(docDelete)
            .commit()

        assertTrue(docSet.get().exists)
        assertEquals("new", docSet.get().getString("status"))
        assertEquals("updated", docUpdate.get().getString("status"))
        assertFalse(docDelete.get().exists)
    }
}
