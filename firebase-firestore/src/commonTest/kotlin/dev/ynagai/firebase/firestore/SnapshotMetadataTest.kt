package dev.ynagai.firebase.firestore

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SnapshotMetadataTest {

    @Test
    fun propertiesAreSet() {
        val metadata = SnapshotMetadata(hasPendingWrites = true, isFromCache = false)
        assertTrue(metadata.hasPendingWrites)
        assertFalse(metadata.isFromCache)
    }

    @Test
    fun equality() {
        val a = SnapshotMetadata(hasPendingWrites = true, isFromCache = true)
        val b = SnapshotMetadata(hasPendingWrites = true, isFromCache = true)
        assertEquals(a, b)
        assertEquals(a.hashCode(), b.hashCode())
    }

    @Test
    fun copyWorks() {
        val original = SnapshotMetadata(hasPendingWrites = true, isFromCache = false)
        val copy = original.copy(isFromCache = true)
        assertTrue(copy.hasPendingWrites)
        assertTrue(copy.isFromCache)
    }
}
