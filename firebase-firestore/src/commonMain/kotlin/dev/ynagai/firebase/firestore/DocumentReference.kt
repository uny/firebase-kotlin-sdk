package dev.ynagai.firebase.firestore

import kotlinx.coroutines.flow.Flow

expect class DocumentReference {
    val id: String
    val path: String
    val parent: CollectionReference
    fun collection(collectionPath: String): CollectionReference
    suspend fun get(source: Source = Source.DEFAULT): DocumentSnapshot
    suspend fun set(data: Map<String, Any?>, merge: Boolean = false): Unit
    suspend fun update(data: Map<String, Any?>): Unit
    suspend fun delete(): Unit
    val snapshots: Flow<DocumentSnapshot>
}
