package dev.ynagai.firebase.firestore

import com.google.firebase.firestore.DocumentChange as AndroidDocumentChange
import com.google.firebase.firestore.QuerySnapshot as AndroidQuerySnapshot

actual class QuerySnapshot internal constructor(
    internal val android: AndroidQuerySnapshot,
) {
    actual val documents: List<DocumentSnapshot>
        get() = android.documents.map { DocumentSnapshot(it) }

    actual val isEmpty: Boolean
        get() = android.isEmpty

    actual val size: Int
        get() = android.size()

    actual val metadata: SnapshotMetadata
        get() = SnapshotMetadata(
            hasPendingWrites = android.metadata.hasPendingWrites(),
            isFromCache = android.metadata.isFromCache,
        )

    actual val documentChanges: List<DocumentChange>
        get() = android.documentChanges.map { it.toCommon() }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is QuerySnapshot) return false
        return android == other.android
    }

    override fun hashCode(): Int = android.hashCode()

    override fun toString(): String = "QuerySnapshot(size=$size)"
}

private fun AndroidDocumentChange.toCommon(): DocumentChange = DocumentChange(
    type = when (type) {
        AndroidDocumentChange.Type.ADDED -> DocumentChange.Type.ADDED
        AndroidDocumentChange.Type.MODIFIED -> DocumentChange.Type.MODIFIED
        AndroidDocumentChange.Type.REMOVED -> DocumentChange.Type.REMOVED
    },
    document = DocumentSnapshot(document),
    oldIndex = oldIndex,
    newIndex = newIndex,
)
