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
}
