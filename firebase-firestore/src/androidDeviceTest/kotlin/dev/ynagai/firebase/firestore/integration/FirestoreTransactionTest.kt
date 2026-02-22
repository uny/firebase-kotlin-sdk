package dev.ynagai.firebase.firestore.integration

import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class FirestoreTransactionTest : FirestoreEmulatorTest() {

    @Test
    fun transactionReadAndWrite() = runTest {
        val docRef = firestore.collection("tx-test").document("counter")
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
        val docRef = firestore.collection("tx-test").document("return-val")
        docRef.set(mapOf("value" to "hello"))

        val value = firestore.runTransaction {
            val snapshot = get(docRef)
            snapshot.getString("value")
        }

        assertEquals("hello", value)
    }

    @Test
    fun transactionDeleteDocument() = runTest {
        val docRef = firestore.collection("tx-test").document("to-delete")
        docRef.set(mapOf("temp" to true))

        firestore.runTransaction {
            delete(docRef)
        }

        val result = docRef.get()
        assertTrue(!result.exists)
    }

    @Test
    fun transactionUpdateDocument() = runTest {
        val docRef = firestore.collection("tx-test").document("to-update")
        docRef.set(mapOf("name" to "Eve", "score" to 100L))

        firestore.runTransaction {
            update(docRef, mapOf("score" to 200L))
        }

        val result = docRef.get()
        assertEquals("Eve", result.getString("name"))
        assertEquals(200L, result.getLong("score"))
    }
}
