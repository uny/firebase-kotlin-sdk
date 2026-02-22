package dev.ynagai.firebase.firestore

import dev.ynagai.firebase.Timestamp

expect class DocumentSnapshot {
    val id: String
    val reference: DocumentReference
    val exists: Boolean
    val metadata: SnapshotMetadata
    fun getData(): Map<String, Any?>?
    fun get(field: String): Any?
    fun getString(field: String): String?
    fun getLong(field: String): Long?
    fun getDouble(field: String): Double?
    fun getBoolean(field: String): Boolean?
    fun getTimestamp(field: String): Timestamp?
    fun getGeoPoint(field: String): GeoPoint?
    fun getBlob(field: String): Blob?
    fun contains(field: String): Boolean
    fun contains(fieldPath: FieldPath): Boolean
}
