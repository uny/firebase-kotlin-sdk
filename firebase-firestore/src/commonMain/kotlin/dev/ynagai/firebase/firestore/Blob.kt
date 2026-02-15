package dev.ynagai.firebase.firestore

/**
 * Represents immutable binary data in Firestore.
 *
 * Not a data class because [ByteArray] uses reference equality by default.
 * Uses defensive copies to ensure immutability.
 */
class Blob(bytes: ByteArray) {
    private val _bytes: ByteArray = bytes.copyOf()

    fun toBytes(): ByteArray = _bytes.copyOf()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Blob) return false
        return _bytes.contentEquals(other._bytes)
    }

    override fun hashCode(): Int = _bytes.contentHashCode()

    override fun toString(): String = "Blob(${_bytes.size} bytes)"
}
