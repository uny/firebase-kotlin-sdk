package dev.ynagai.firebase.auth

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNull

class UserMetadataTest {

    @Test
    fun equalMetadataAreEqual() {
        val m1 = UserMetadata(creationTimestamp = 1000L, lastSignInTimestamp = 2000L)
        val m2 = UserMetadata(creationTimestamp = 1000L, lastSignInTimestamp = 2000L)
        assertEquals(m1, m2)
    }

    @Test
    fun differentMetadataAreNotEqual() {
        val m1 = UserMetadata(creationTimestamp = 1000L, lastSignInTimestamp = 2000L)
        val m2 = UserMetadata(creationTimestamp = 1000L, lastSignInTimestamp = 3000L)
        assertNotEquals(m1, m2)
    }

    @Test
    fun nullTimestampsAreHandled() {
        val metadata = UserMetadata(creationTimestamp = null, lastSignInTimestamp = null)
        assertNull(metadata.creationTimestamp)
        assertNull(metadata.lastSignInTimestamp)
    }

    @Test
    fun partialNullTimestamps() {
        val metadata = UserMetadata(creationTimestamp = 1000L, lastSignInTimestamp = null)
        assertEquals(1000L, metadata.creationTimestamp)
        assertNull(metadata.lastSignInTimestamp)
    }

    @Test
    fun hashCodeIsConsistent() {
        val m1 = UserMetadata(creationTimestamp = 1000L, lastSignInTimestamp = 2000L)
        val m2 = UserMetadata(creationTimestamp = 1000L, lastSignInTimestamp = 2000L)
        assertEquals(m1.hashCode(), m2.hashCode())
    }
}
