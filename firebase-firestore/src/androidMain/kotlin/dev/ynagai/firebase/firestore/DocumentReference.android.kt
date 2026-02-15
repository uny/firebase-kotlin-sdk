package dev.ynagai.firebase.firestore

import com.google.firebase.firestore.DocumentReference as AndroidDocumentReference
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

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
        DocumentSnapshot(android.get(source.toAndroid()).await())

    actual suspend fun set(data: Map<String, Any?>, merge: Boolean) {
        if (merge) {
            android.set(data.toAndroidData(), SetOptions.merge()).await()
        } else {
            android.set(data.toAndroidData()).await()
        }
    }

    actual suspend fun set(data: Map<String, Any?>, options: dev.ynagai.firebase.firestore.SetOptions) {
        when (options) {
            is dev.ynagai.firebase.firestore.SetOptions.Overwrite -> android.set(data.toAndroidData()).await()
            is dev.ynagai.firebase.firestore.SetOptions.Merge -> android.set(data.toAndroidData(), SetOptions.merge()).await()
            is dev.ynagai.firebase.firestore.SetOptions.MergeFields -> android.set(data.toAndroidData(), SetOptions.mergeFields(options.fields)).await()
        }
    }

    actual suspend fun update(data: Map<String, Any?>) {
        android.update(data.toAndroidData()).await()
    }

    actual suspend fun delete() {
        android.delete().await()
    }

    actual val snapshots: Flow<DocumentSnapshot>
        get() = callbackFlow {
            val listener = android.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error.toCommon())
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
                    close(error.toCommon())
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
