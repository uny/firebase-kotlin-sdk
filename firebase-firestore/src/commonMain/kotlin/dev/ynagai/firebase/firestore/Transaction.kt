package dev.ynagai.firebase.firestore

expect class Transaction {
    fun get(documentRef: DocumentReference): DocumentSnapshot
    fun set(documentRef: DocumentReference, data: Map<String, Any?>, merge: Boolean = false): Transaction
    fun set(documentRef: DocumentReference, data: Map<String, Any?>, options: SetOptions): Transaction
    fun update(documentRef: DocumentReference, data: Map<String, Any?>): Transaction
    fun delete(documentRef: DocumentReference): Transaction
}
