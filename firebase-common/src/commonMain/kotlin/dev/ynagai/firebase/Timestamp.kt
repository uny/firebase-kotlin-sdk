package dev.ynagai.firebase

data class Timestamp(
    val seconds: Long,
    val nanoseconds: Int,
) : Comparable<Timestamp> {

    init {
        require(nanoseconds in 0..999_999_999) {
            "nanoseconds must be between 0 and 999,999,999"
        }
    }

    override fun compareTo(other: Timestamp): Int {
        val secondsCompare = seconds.compareTo(other.seconds)
        return if (secondsCompare != 0) secondsCompare else nanoseconds.compareTo(other.nanoseconds)
    }

    fun toMillis(): Long = seconds * 1000 + nanoseconds / 1_000_000

    companion object {
        fun fromMillis(milliseconds: Long): Timestamp {
            val seconds = milliseconds / 1000
            val nanos = ((milliseconds % 1000) * 1_000_000).toInt()
            return if (nanos < 0) {
                Timestamp(seconds - 1, nanos + 1_000_000_000)
            } else {
                Timestamp(seconds, nanos)
            }
        }
    }
}
