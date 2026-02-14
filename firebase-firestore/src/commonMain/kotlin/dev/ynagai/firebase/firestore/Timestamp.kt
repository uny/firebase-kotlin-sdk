package dev.ynagai.firebase.firestore

/**
 * A Firestore Timestamp represents a point in time with nanosecond precision.
 *
 * @property seconds The number of seconds since Unix epoch (1970-01-01T00:00:00Z).
 * @property nanoseconds The non-negative fractions of a second at nanosecond resolution.
 */
data class Timestamp(
    val seconds: Long,
    val nanoseconds: Int,
) : Comparable<Timestamp> {
    override fun compareTo(other: Timestamp): Int {
        val secondsCompare = seconds.compareTo(other.seconds)
        return if (secondsCompare != 0) secondsCompare else nanoseconds.compareTo(other.nanoseconds)
    }
}
