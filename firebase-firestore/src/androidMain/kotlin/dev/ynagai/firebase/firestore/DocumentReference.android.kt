package dev.ynagai.firebase.firestore

import com.google.firebase.firestore.DocumentReference as AndroidDocumentReference
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

actual class DocumentReference internal constructor(
    internal val android: AndroidDocumentReference,
) {
    actual val id: String
        get() = android.id

    actual val path: String
        get() = android.path

    actual val parent: CollectionReference
        get() = CollectionReference(android.parent)

    actual fun collection(collectionPath: String): CollectionReference =
        CollectionReference(android.collection(collectionPath))

    actual suspend fun get(source: Source): DocumentSnapshot =
        DocumentSnapshot(android.get(source.toAndroid()).awaitWithWrappedExceptions())

    actual suspend fun set(data: Map<String, Any?>, merge: Boolean) {
        if (merge) {
            android.set(data.toAndroidData(), SetOptions.merge()).awaitWithWrappedExceptions()
        } else {
            android.set(data.toAndroidData()).awaitWithWrappedExceptions()
        }
    }

    actual suspend fun set(data: Map<String, Any?>, options: dev.ynagai.firebase.firestore.SetOptions) {
        when (options) {
            is dev.ynagai.firebase.firestore.SetOptions.Overwrite -> android.set(data.toAndroidData()).awaitWithWrappedExceptions()
            is dev.ynagai.firebase.firestore.SetOptions.Merge -> android.set(data.toAndroidData(), SetOptions.merge()).awaitWithWrappedExceptions()
            is dev.ynagai.firebase.firestore.SetOptions.MergeFields -> android.set(data.toAndroidData(), SetOptions.mergeFields(options.fields)).awaitWithWrappedExceptions()
        }
    }

    actual suspend fun update(data: Map<String, Any?>) {
        android.update(data.toAndroidData()).awaitWithWrappedExceptions()
    }

    actual suspend fun delete() {
        android.delete().awaitWithWrappedExceptions()
    }

    actual val snapshots: Flow<DocumentSnapshot>
        get() = callbackFlow {
            val listener = android.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error.toCommonFirestoreException())
                } else if (snapshot != null) {
                    trySend(DocumentSnapshot(snapshot))
                }
            }
            awaitClose { listener.remove() }
        }

    actual fun snapshots(metadataChanges: MetadataChanges): Flow<DocumentSnapshot> =
        callbackFlow {
            val listener = android.addSnapshotListener(metadataChanges.toAndroid()) { snapshot, error ->
                if (error != null) {
                    close(error.toCommonFirestoreException())
                } else if (snapshot != null) {
                    trySend(DocumentSnapshot(snapshot))
                }
            }
            awaitClose { listener.remove() }
        }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is DocumentReference) return false
        return android == other.android
    }

    override fun hashCode(): Int = android.hashCode()

    override fun toString(): String = "DocumentReference(path=$path)"
}
