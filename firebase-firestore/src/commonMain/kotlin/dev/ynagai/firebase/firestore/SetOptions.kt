package dev.ynagai.firebase.firestore

/**
 * Options for controlling how [DocumentReference.set], [Transaction.set],
 * and [WriteBatch.set] behave.
 */
sealed class SetOptions {
    internal object Overwrite : SetOptions()
    internal object Merge : SetOptions()
    internal data class MergeFields(val fields: List<String>) : SetOptions()

    companion object {
        /** Merge the provided data with the existing document. */
        fun merge(): SetOptions = Merge

        /** Merge only the specified fields of the provided data. */
        fun mergeFields(fields: List<String>): SetOptions = MergeFields(fields)

        /** Merge only the specified fields of the provided data. */
        fun mergeFields(vararg fields: String): SetOptions = MergeFields(fields.toList())
    }
}
