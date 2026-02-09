package dev.ynagai.firebase.firestore

import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCObjectVar
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.value
import platform.Foundation.NSError
import swiftPMImport.dev.ynagai.firebase.firebase.firestore.FIRTransaction

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
actual class Transaction internal constructor(
    internal val apple: FIRTransaction,
) {
    actual fun get(documentRef: DocumentReference): DocumentSnapshot =
        memScoped {
            val errorPtr = alloc<ObjCObjectVar<NSError?>>()
            val snapshot = apple.getDocument(documentRef.apple, error = errorPtr.ptr)
            errorPtr.value?.let { throw it.toException() }
            if (snapshot == null) {
                throw FirebaseFirestoreException(
                    "Failed to get document in transaction: document is null and no error was provided.",
                    FirestoreExceptionCode.UNKNOWN,
                )
            }
            DocumentSnapshot(snapshot)
        }

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
