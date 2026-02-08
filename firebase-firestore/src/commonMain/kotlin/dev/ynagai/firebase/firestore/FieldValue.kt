package dev.ynagai.firebase.firestore

expect class FieldValue {
    companion object {
        fun serverTimestamp(): FieldValue
        fun increment(value: Long): FieldValue
        fun increment(value: Double): FieldValue
        fun arrayUnion(vararg elements: Any): FieldValue
        fun arrayRemove(vararg elements: Any): FieldValue
        fun delete(): FieldValue
    }
}
