package dev.ynagai.firebase

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
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

    @Test
    fun toMillisPositive() {
        val ts = Timestamp(seconds = 1707994800L, nanoseconds = 500_000_000)
        assertEquals(1707994800_500L, ts.toMillis())
    }

    @Test
    fun toMillisZero() {
        val ts = Timestamp(seconds = 0L, nanoseconds = 0)
        assertEquals(0L, ts.toMillis())
    }

    @Test
    fun toMillisTruncatesSubMillisecondNanos() {
        val ts = Timestamp(seconds = 1L, nanoseconds = 1_500_999)
        assertEquals(1001L, ts.toMillis())
    }

    @Test
    fun fromMillisPositive() {
        val ts = Timestamp.fromMillis(1707994800_500L)
        assertEquals(1707994800L, ts.seconds)
        assertEquals(500_000_000, ts.nanoseconds)
    }

    @Test
    fun fromMillisZero() {
        val ts = Timestamp.fromMillis(0L)
        assertEquals(0L, ts.seconds)
        assertEquals(0, ts.nanoseconds)
    }

    @Test
    fun fromMillisNegative() {
        val ts = Timestamp.fromMillis(-1500L)
        assertEquals(-2L, ts.seconds)
        assertEquals(500_000_000, ts.nanoseconds)
    }

    @Test
    fun fromMillisRoundTrip() {
        val millis = 1707994800_123L
        val ts = Timestamp.fromMillis(millis)
        assertEquals(millis, ts.toMillis())
    }

    @Test
    fun nanosecondsValidationRejectsNegative() {
        assertFailsWith<IllegalArgumentException> {
            Timestamp(seconds = 0L, nanoseconds = -1)
        }
    }

    @Test
    fun nanosecondsValidationRejectsOverflow() {
        assertFailsWith<IllegalArgumentException> {
            Timestamp(seconds = 0L, nanoseconds = 1_000_000_000)
        }
    }

    // --- toInstant ---

    @Test
    fun toInstantBasic() {
        val ts = Timestamp(seconds = 1707994800L, nanoseconds = 500_000_000)
        val instant = ts.toInstant()
        assertEquals(1707994800L, instant.epochSeconds)
        assertEquals(500_000_000, instant.nanosecondsOfSecond)
    }

    @Test
    fun toInstantZero() {
        val ts = Timestamp(seconds = 0L, nanoseconds = 0)
        val instant = ts.toInstant()
        assertEquals(Instant.fromEpochSeconds(0, 0), instant)
    }

    @Test
    fun toInstantNegativeSeconds() {
        val ts = Timestamp(seconds = -100L, nanoseconds = 123_456_789)
        val instant = ts.toInstant()
        assertEquals(-100L, instant.epochSeconds)
        assertEquals(123_456_789, instant.nanosecondsOfSecond)
    }

    @Test
    fun toInstantMaxNanoseconds() {
        val ts = Timestamp(seconds = 1L, nanoseconds = 999_999_999)
        val instant = ts.toInstant()
        assertEquals(1L, instant.epochSeconds)
        assertEquals(999_999_999, instant.nanosecondsOfSecond)
    }

    // --- fromInstant ---

    @Test
    fun fromInstantBasic() {
        val instant = Instant.fromEpochSeconds(1707994800L, 500_000_000)
        val ts = Timestamp.fromInstant(instant)
        assertEquals(1707994800L, ts.seconds)
        assertEquals(500_000_000, ts.nanoseconds)
    }

    @Test
    fun fromInstantZero() {
        val instant = Instant.fromEpochSeconds(0, 0)
        val ts = Timestamp.fromInstant(instant)
        assertEquals(0L, ts.seconds)
        assertEquals(0, ts.nanoseconds)
    }

    // --- round-trip ---

    @Test
    fun toInstantRoundTrip() {
        val original = Timestamp(seconds = 1707994800L, nanoseconds = 123_456_789)
        val roundTripped = Timestamp.fromInstant(original.toInstant())
        assertEquals(original, roundTripped)
    }

    @Test
    fun fromInstantRoundTrip() {
        val original = Instant.fromEpochSeconds(1707994800L, 123_456_789)
        val roundTripped = Timestamp.fromInstant(original).toInstant()
        assertEquals(original, roundTripped)
    }

    // --- now ---

    @Test
    fun nowReturnsRecentTimestamp() {
        val before = Clock.System.now()
        val ts = Timestamp.now()
        val after = Clock.System.now()
        val tsInstant = ts.toInstant()
        assertTrue(tsInstant >= before, "now() should be >= clock before")
        assertTrue(tsInstant <= after, "now() should be <= clock after")
    }

    @Test
    fun nowReturnsNonZeroTimestamp() {
        val ts = Timestamp.now()
        assertTrue(ts.seconds > 0, "now() seconds should be positive")
    }

    @Test
    fun nanosecondsValidationAcceptsBoundary() {
        val zero = Timestamp(seconds = 0L, nanoseconds = 0)
        assertEquals(0, zero.nanoseconds)

        val max = Timestamp(seconds = 0L, nanoseconds = 999_999_999)
        assertEquals(999_999_999, max.nanoseconds)
    }
}
