package dev.ynagai.firebase.firestore

/**
 * Metadata about a snapshot, describing the state of the snapshot.
 *
 * @property hasPendingWrites Whether the snapshot contains the result of local writes
 *                            that have not yet been committed to the backend.
 * @property isFromCache Whether the snapshot was created from cached data rather than
 *                       guaranteed up-to-date server data.
 */
data class SnapshotMetadata(
    val hasPendingWrites: Boolean,
    val isFromCache: Boolean,
)
