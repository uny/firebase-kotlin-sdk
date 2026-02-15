package dev.ynagai.firebase.firestore

expect class WriteBatch {
    fun set(documentRef: DocumentReference, data: Map<String, Any?>, merge: Boolean = false): WriteBatch
    fun set(documentRef: DocumentReference, data: Map<String, Any?>, options: SetOptions): WriteBatch
    fun update(documentRef: DocumentReference, data: Map<String, Any?>): WriteBatch
    fun delete(documentRef: DocumentReference): WriteBatch
    suspend fun commit()
}
