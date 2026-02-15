package dev.ynagai.firebase.firestore

/**
 * Represents a change to a document in a query result set.
 *
 * @property type The type of change (added, modified, or removed).
 * @property document The document affected by this change.
 * @property oldIndex The index of the document in the old query result set,
 *                    or -1 if the document was added.
 * @property newIndex The index of the document in the new query result set,
 *                    or -1 if the document was removed.
 */
data class DocumentChange(
    val type: Type,
    val document: DocumentSnapshot,
    val oldIndex: Int,
    val newIndex: Int,
) {
    /**
     * The type of change that occurred on a document.
     */
    enum class Type { ADDED, MODIFIED, REMOVED }
}
