package dev.ynagai.firebase.firestore

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.suspendCancellableCoroutine
import swiftPMImport.dev.ynagai.firebase.firebase.firestore.FIRCollectionReference
import swiftPMImport.dev.ynagai.firebase.firebase.firestore.FIRDocumentReference
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@OptIn(ExperimentalForeignApi::class)
actual class CollectionReference internal constructor(
    internal val appleCollection: FIRCollectionReference,
) : Query(appleCollection) {
    actual val id: String
        get() = appleCollection.collectionID

    actual val path: String
        get() = appleCollection.path

    actual val parent: DocumentReference?
        get() = appleCollection.parent?.let { DocumentReference(it) }

    actual fun document(documentPath: String): DocumentReference =
        if (documentPath.isEmpty()) {
            DocumentReference(appleCollection.documentWithAutoID())
        } else {
            DocumentReference(appleCollection.documentWithPath(documentPath))
        }

    actual suspend fun add(data: Map<String, Any?>): DocumentReference =
        suspendCancellableCoroutine { continuation ->
            var docRef: FIRDocumentReference? = null
            docRef = appleCollection.addDocumentWithData(
                data.toAppleData(),
            ) { error ->
                if (error != null) {
                    continuation.resumeWithException(error.toException())
                } else {
                    val currentDocRef = docRef
                    if (currentDocRef != null) {
                        continuation.resume(DocumentReference(currentDocRef))
                    } else {
                        continuation.resumeWithException(
                            FirebaseFirestoreException(
                                "addDocumentWithData completed successfully but returned a null document reference.",
                                FirestoreExceptionCode.UNKNOWN,
                            ),
                        )
                    }
                }
            }
        }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CollectionReference) return false
        return path == other.path
    }

    override fun hashCode(): Int = path.hashCode()

    override fun toString(): String = "CollectionReference(path=$path)"
}
