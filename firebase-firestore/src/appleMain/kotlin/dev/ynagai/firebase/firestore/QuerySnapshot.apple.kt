package dev.ynagai.firebase.firestore

import kotlinx.cinterop.ExperimentalForeignApi
import swiftPMImport.dev.ynagai.firebase.firebase.firestore.FIRDocumentSnapshot
import swiftPMImport.dev.ynagai.firebase.firebase.firestore.FIRQuerySnapshot

@OptIn(ExperimentalForeignApi::class)
actual class QuerySnapshot internal constructor(
    internal val apple: FIRQuerySnapshot,
) {
    @Suppress("UNCHECKED_CAST")
    actual val documents: List<DocumentSnapshot>
        get() = (apple.documents as List<FIRDocumentSnapshot>).map { DocumentSnapshot(it) }

    actual val isEmpty: Boolean
        get() = apple.documents.isEmpty()

    actual val size: Int
        get() = apple.documents.size
}
