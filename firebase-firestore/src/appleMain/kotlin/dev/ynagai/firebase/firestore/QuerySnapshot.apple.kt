package dev.ynagai.firebase.firestore

import kotlinx.cinterop.ExperimentalForeignApi
import swiftPMImport.dev.ynagai.firebase.firebase.firestore.FIRDocumentChange
import swiftPMImport.dev.ynagai.firebase.firebase.firestore.FIRDocumentChangeType
import swiftPMImport.dev.ynagai.firebase.firebase.firestore.FIRDocumentSnapshot
import swiftPMImport.dev.ynagai.firebase.firebase.firestore.FIRQuerySnapshot

@OptIn(ExperimentalForeignApi::class)
actual class QuerySnapshot internal constructor(
    internal val apple: FIRQuerySnapshot,
) {
    actual val documents: List<DocumentSnapshot>
        get() = apple.documents.filterIsInstance<FIRDocumentSnapshot>().map { DocumentSnapshot(it) }

    actual val isEmpty: Boolean
        get() = apple.documents.isEmpty()

    actual val size: Int
        get() = apple.documents.size

    actual val metadata: SnapshotMetadata
        get() = apple.metadata.let {
            SnapshotMetadata(
                hasPendingWrites = it.hasPendingWrites(),
                isFromCache = it.isFromCache(),
            )
        }

    actual val documentChanges: List<DocumentChange>
        get() = apple.documentChanges.filterIsInstance<FIRDocumentChange>().map { it.toCommon() }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is QuerySnapshot) return false
        return apple == other.apple
    }

    override fun hashCode(): Int = apple.hash.toInt()

    override fun toString(): String = "QuerySnapshot(size=$size)"
}

@OptIn(ExperimentalForeignApi::class)
@Suppress("DEPRECATION")
private fun FIRDocumentChange.toCommon(): DocumentChange {
    val changeType = when (type) {
        FIRDocumentChangeType.FIRDocumentChangeTypeAdded -> DocumentChange.Type.ADDED
        FIRDocumentChangeType.FIRDocumentChangeTypeModified -> DocumentChange.Type.MODIFIED
        FIRDocumentChangeType.FIRDocumentChangeTypeRemoved -> DocumentChange.Type.REMOVED
        else -> DocumentChange.Type.MODIFIED
    }
    return DocumentChange(
        type = changeType,
        document = DocumentSnapshot(document),
        oldIndex = oldIndex.toInt(),
        newIndex = newIndex.toInt(),
    )
}
