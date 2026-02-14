package dev.ynagai.firebase.firestore

expect class DocumentSnapshot {
    val id: String
    val reference: DocumentReference
    val exists: Boolean
    fun getData(): Map<String, Any?>?
    fun get(field: String): Any?
    fun getString(field: String): String?
    fun getLong(field: String): Long?
    fun getDouble(field: String): Double?
    fun getBoolean(field: String): Boolean?
    fun getTimestamp(field: String): Timestamp?
}
