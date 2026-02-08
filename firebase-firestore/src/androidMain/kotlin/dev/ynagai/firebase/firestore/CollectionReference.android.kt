package dev.ynagai.firebase.firestore

import com.google.firebase.firestore.CollectionReference as AndroidCollectionReference
import kotlinx.coroutines.tasks.await

actual class CollectionReference internal constructor(
    internal val androidCollection: AndroidCollectionReference,
) : Query(androidCollection) {
    actual val id: String
        get() = androidCollection.id

    actual val path: String
        get() = androidCollection.path

    actual val parent: DocumentReference?
        get() = androidCollection.parent?.let { DocumentReference(it) }

    actual fun document(documentPath: String): DocumentReference =
        if (documentPath.isEmpty()) {
            DocumentReference(androidCollection.document())
        } else {
            DocumentReference(androidCollection.document(documentPath))
        }

    actual suspend fun add(data: Map<String, Any?>): DocumentReference =
        DocumentReference(androidCollection.add(data.toAndroidData()).await())
}
