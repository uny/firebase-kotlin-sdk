package dev.ynagai.firebase.firestore

import dev.ynagai.firebase.Firebase
import dev.ynagai.firebase.FirebaseApp

expect val Firebase.firestore: FirebaseFirestore
expect fun Firebase.firestore(app: FirebaseApp): FirebaseFirestore

expect class FirebaseFirestore {
    fun collection(collectionPath: String): CollectionReference
    fun document(documentPath: String): DocumentReference
    fun collectionGroup(collectionId: String): Query
    fun batch(): WriteBatch
    suspend fun <T> runTransaction(func: Transaction.() -> T): T
    fun useEmulator(host: String, port: Int)
    suspend fun clearPersistence()
    suspend fun disableNetwork()
    suspend fun enableNetwork()
}
