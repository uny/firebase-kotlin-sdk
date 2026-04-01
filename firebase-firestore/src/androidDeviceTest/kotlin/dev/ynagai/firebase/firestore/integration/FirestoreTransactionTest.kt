package dev.ynagai.firebase.firestore.integration

import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class FirestoreTransactionTest : FirestoreEmulatorTest() {

    private val collectionPath = "tx-test-${System.nanoTime()}"

    @Test
    fun transactionReadAndWrite() = runTest {
        val docRef = firestore.collection(collectionPath).document("counter")
        docRef.set(mapOf("count" to 10L))

        firestore.runTransaction {
            val snapshot = get(docRef)
            val currentCount = snapshot.getLong("count") ?: 0L
            set(docRef, mapOf("count" to currentCount + 1))
        }

        val result = docRef.get()
        assertEquals(11L, result.getLong("count"))
    }

    @Test
    fun transactionReturnsValue() = runTest {
        val docRef = firestore.collection(collectionPath).document("return-val")
        docRef.set(mapOf("value" to "hello"))

        val value = firestore.runTransaction {
            val snapshot = get(docRef)
            snapshot.getString("value")
        }

        assertEquals("hello", value)
    }

    @Test
    fun transactionDeleteDocument() = runTest {
        val docRef = firestore.collection(collectionPath).document("to-delete")
        docRef.set(mapOf("temp" to true))

        firestore.runTransaction {
            delete(docRef)
        }

        val result = docRef.get()
        assertFalse(result.exists)
    }

    @Test
    fun transactionQueryRead() = runTest {
        val col = firestore.collection("tx-query-${System.nanoTime()}")
        col.document("a").set(mapOf("category" to "fruit", "name" to "apple"))
        col.document("b").set(mapOf("category" to "fruit", "name" to "banana"))
        col.document("c").set(mapOf("category" to "veggie", "name" to "carrot"))

        val query = col.where("category", equalTo = "fruit")

        val count = firestore.runTransaction {
            val snapshot = get(query)
            val size = snapshot.size
            set(
                col.document("summary"),
                mapOf("fruitCount" to size.toLong()),
            )
            size
        }

        assertEquals(2, count)
        val summary = col.document("summary").get()
        assertEquals(2L, summary.getLong("fruitCount"))
    }

    @Test
    fun transactionUpdateDocument() = runTest {
        val docRef = firestore.collection(collectionPath).document("to-update")
        docRef.set(mapOf("name" to "Eve", "score" to 100L))

        firestore.runTransaction {
            update(docRef, mapOf("score" to 200L))
        }

        val result = docRef.get()
        assertEquals("Eve", result.getString("name"))
        assertEquals(200L, result.getLong("score"))
    }
}
