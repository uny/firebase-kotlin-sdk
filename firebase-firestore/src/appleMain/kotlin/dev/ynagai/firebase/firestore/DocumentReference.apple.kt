package dev.ynagai.firebase.firestore

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import swiftPMImport.dev.ynagai.firebase.firebase.firestore.FIRDocumentReference
import swiftPMImport.dev.ynagai.firebase.firebase.firestore.FIRDocumentSnapshot

@OptIn(ExperimentalForeignApi::class)
actual class DocumentReference internal constructor(
    internal val apple: FIRDocumentReference,
) {
    actual val id: String
        get() = apple.documentID

    actual val path: String
        get() = apple.path

    actual val parent: CollectionReference
        get() = CollectionReference(apple.parent)

    actual fun collection(collectionPath: String): CollectionReference =
        CollectionReference(apple.collectionWithPath(collectionPath))

    actual suspend fun get(source: Source): DocumentSnapshot {
        val result = awaitResult<FIRDocumentSnapshot> { callback ->
            apple.getDocumentWithSource(source.toApple(), completion = callback)
        }
        return DocumentSnapshot(result)
    }

    actual suspend fun set(data: Map<String, Any?>, merge: Boolean) {
        if (merge) {
            await { callback -> apple.setData(data.toAppleData(), merge = true, completion = callback) }
        } else {
            await { callback -> apple.setData(data.toAppleData(), completion = callback) }
        }
    }

    actual suspend fun set(data: Map<String, Any?>, options: SetOptions) {
        when (options) {
            is SetOptions.Overwrite -> await { callback -> apple.setData(data.toAppleData(), completion = callback) }
            is SetOptions.Merge -> await { callback -> apple.setData(data.toAppleData(), merge = true, completion = callback) }
            is SetOptions.MergeFields -> await { callback -> apple.setData(data.toAppleData(), mergeFields = options.fields, completion = callback) }
        }
    }

    actual suspend fun update(data: Map<String, Any?>) {
        await { callback -> apple.updateData(data.toAppleData(), completion = callback) }
    }

    actual suspend fun delete() {
        await { callback -> apple.deleteDocumentWithCompletion(callback) }
    }

    actual val snapshots: Flow<DocumentSnapshot>
        get() = callbackFlow {
            val listener = apple.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error.toException())
                } else if (snapshot != null) {
                    trySend(DocumentSnapshot(snapshot))
                }
            }
            awaitClose { listener.remove() }
        }

    actual fun snapshots(metadataChanges: MetadataChanges): Flow<DocumentSnapshot> =
        callbackFlow {
            val listener = apple.addSnapshotListenerWithIncludeMetadataChanges(
                metadataChanges == MetadataChanges.INCLUDE
            ) { snapshot, error ->
                if (error != null) {
                    close(error.toException())
                } else if (snapshot != null) {
                    trySend(DocumentSnapshot(snapshot))
                }
            }
            awaitClose { listener.remove() }
        }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is DocumentReference) return false
        return path == other.path
    }

    override fun hashCode(): Int = path.hashCode()

    override fun toString(): String = "DocumentReference(path=$path)"
}
