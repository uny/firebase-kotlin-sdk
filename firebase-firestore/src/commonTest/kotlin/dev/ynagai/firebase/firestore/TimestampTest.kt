package dev.ynagai.firebase.firestore

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TimestampTest {

    @Test
    fun equalTimestampsAreEqual() {
        val t1 = Timestamp(seconds = 1000L, nanoseconds = 500)
        val t2 = Timestamp(seconds = 1000L, nanoseconds = 500)
        assertEquals(t1, t2)
    }

    @Test
    fun compareBySeconds() {
        val earlier = Timestamp(seconds = 1000L, nanoseconds = 0)
        val later = Timestamp(seconds = 2000L, nanoseconds = 0)
        assertTrue(earlier < later)
        assertTrue(later > earlier)
    }

    @Test
    fun compareByNanoseconds() {
        val earlier = Timestamp(seconds = 1000L, nanoseconds = 100)
        val later = Timestamp(seconds = 1000L, nanoseconds = 200)
        assertTrue(earlier < later)
        assertTrue(later > earlier)
    }

    @Test
    fun compareEqualTimestamps() {
        val t1 = Timestamp(seconds = 1000L, nanoseconds = 500)
        val t2 = Timestamp(seconds = 1000L, nanoseconds = 500)
        assertEquals(0, t1.compareTo(t2))
    }

    @Test
    fun sortTimestamps() {
        val timestamps = listOf(
            Timestamp(seconds = 3L, nanoseconds = 0),
            Timestamp(seconds = 1L, nanoseconds = 0),
            Timestamp(seconds = 2L, nanoseconds = 0),
        )
        val sorted = timestamps.sorted()
        assertEquals(1L, sorted[0].seconds)
        assertEquals(2L, sorted[1].seconds)
        assertEquals(3L, sorted[2].seconds)
    }

    @Test
    fun dataCopyWorks() {
        val original = Timestamp(seconds = 1000L, nanoseconds = 500)
        val copy = original.copy(nanoseconds = 999)
        assertEquals(1000L, copy.seconds)
        assertEquals(999, copy.nanoseconds)
    }

    @Test
    fun hashCodeIsConsistent() {
        val t1 = Timestamp(seconds = 1000L, nanoseconds = 500)
        val t2 = Timestamp(seconds = 1000L, nanoseconds = 500)
        assertEquals(t1.hashCode(), t2.hashCode())
    }
}
