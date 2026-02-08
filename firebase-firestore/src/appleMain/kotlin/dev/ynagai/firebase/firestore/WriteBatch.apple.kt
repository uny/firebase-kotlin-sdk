package dev.ynagai.firebase.firestore

import kotlinx.cinterop.ExperimentalForeignApi
import swiftPMImport.dev.ynagai.firebase.firebase.firestore.FIRWriteBatch

@OptIn(ExperimentalForeignApi::class)
actual class WriteBatch internal constructor(
    internal val apple: FIRWriteBatch,
) {
    actual fun set(documentRef: DocumentReference, data: Map<String, Any?>, merge: Boolean): WriteBatch {
        if (merge) {
            apple.setData(data.toAppleData(), forDocument = documentRef.apple, merge = true)
        } else {
            apple.setData(data.toAppleData(), forDocument = documentRef.apple)
        }
        return this
    }

    actual fun update(documentRef: DocumentReference, data: Map<String, Any?>): WriteBatch {
        apple.updateData(data.toAppleData(), forDocument = documentRef.apple)
        return this
    }

    actual fun delete(documentRef: DocumentReference): WriteBatch {
        apple.deleteDocument(documentRef.apple)
        return this
    }

    actual suspend fun commit() {
        await { callback -> apple.commitWithCompletion(callback) }
    }
}
