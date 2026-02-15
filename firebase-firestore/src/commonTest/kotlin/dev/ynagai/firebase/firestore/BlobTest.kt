package dev.ynagai.firebase.firestore

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class BlobTest {

    @Test
    fun equalBlobsAreEqual() {
        val b1 = Blob(byteArrayOf(1, 2, 3))
        val b2 = Blob(byteArrayOf(1, 2, 3))
        assertEquals(b1, b2)
    }

    @Test
    fun differentBlobsAreNotEqual() {
        val b1 = Blob(byteArrayOf(1, 2, 3))
        val b2 = Blob(byteArrayOf(4, 5, 6))
        assertNotEquals(b1, b2)
    }

    @Test
    fun hashCodeIsConsistent() {
        val b1 = Blob(byteArrayOf(1, 2, 3))
        val b2 = Blob(byteArrayOf(1, 2, 3))
        assertEquals(b1.hashCode(), b2.hashCode())
    }

    @Test
    fun toBytesReturnsCopy() {
        val original = byteArrayOf(1, 2, 3)
        val blob = Blob(original)
        val bytes = blob.toBytes()
        assertTrue(original.contentEquals(bytes))
        // Modifying the returned bytes should not affect the Blob
        bytes[0] = 99
        assertEquals(1, blob.toBytes()[0])
    }

    @Test
    fun constructorMakesDefensiveCopy() {
        val original = byteArrayOf(1, 2, 3)
        val blob = Blob(original)
        // Modifying the original array should not affect the Blob
        original[0] = 99
        assertEquals(1, blob.toBytes()[0])
    }

    @Test
    fun emptyBlob() {
        val blob = Blob(byteArrayOf())
        assertEquals(0, blob.toBytes().size)
        assertEquals("Blob(0 bytes)", blob.toString())
    }

    @Test
    fun toStringShowsByteCount() {
        val blob = Blob(byteArrayOf(1, 2, 3, 4, 5))
        assertEquals("Blob(5 bytes)", blob.toString())
    }
}
