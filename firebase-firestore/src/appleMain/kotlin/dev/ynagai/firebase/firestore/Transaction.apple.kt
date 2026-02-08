package dev.ynagai.firebase.firestore

import kotlinx.cinterop.ExperimentalForeignApi
import swiftPMImport.dev.ynagai.firebase.firebase.firestore.FIRTransaction

@OptIn(ExperimentalForeignApi::class)
actual class Transaction internal constructor(
    internal val apple: FIRTransaction,
) {
    actual fun get(documentRef: DocumentReference): DocumentSnapshot =
        DocumentSnapshot(apple.getDocument(documentRef.apple, error = null)!!)

    actual fun set(documentRef: DocumentReference, data: Map<String, Any?>, merge: Boolean): Transaction {
        if (merge) {
            apple.setData(data.toAppleData(), forDocument = documentRef.apple, merge = true)
        } else {
            apple.setData(data.toAppleData(), forDocument = documentRef.apple)
        }
        return this
    }

    actual fun update(documentRef: DocumentReference, data: Map<String, Any?>): Transaction {
        apple.updateData(data.toAppleData(), forDocument = documentRef.apple)
        return this
    }

    actual fun delete(documentRef: DocumentReference): Transaction {
        apple.deleteDocument(documentRef.apple)
        return this
    }
}
