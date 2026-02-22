package dev.ynagai.firebase.firestore

import dev.ynagai.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot as AndroidDocumentSnapshot

actual class DocumentSnapshot internal constructor(
    internal val android: AndroidDocumentSnapshot,
) {
    actual val id: String
        get() = android.id

    actual val reference: DocumentReference
        get() = DocumentReference(android.reference)

    actual val exists: Boolean
        get() = android.exists()

    actual val metadata: SnapshotMetadata
        get() = SnapshotMetadata(
            hasPendingWrites = android.metadata.hasPendingWrites(),
            isFromCache = android.metadata.isFromCache,
        )

    actual fun getData(): Map<String, Any?>? = android.data

    actual fun get(field: String): Any? = android.get(field)

    actual fun getString(field: String): String? = android.getString(field)

    actual fun getLong(field: String): Long? = android.getLong(field)

    actual fun getDouble(field: String): Double? = android.getDouble(field)

    actual fun getBoolean(field: String): Boolean? = android.getBoolean(field)

    actual fun getTimestamp(field: String): Timestamp? =
        android.getTimestamp(field)?.let {
            Timestamp(seconds = it.seconds, nanoseconds = it.nanoseconds)
        }

    actual fun getGeoPoint(field: String): GeoPoint? =
        android.getGeoPoint(field)?.let {
            GeoPoint(latitude = it.latitude, longitude = it.longitude)
        }

    actual fun getBlob(field: String): Blob? =
        android.getBlob(field)?.let {
            Blob(it.toBytes())
        }

    actual fun contains(field: String): Boolean = android.contains(field)

    actual fun contains(fieldPath: FieldPath): Boolean = android.contains(fieldPath.android)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is DocumentSnapshot) return false
        return android == other.android
    }

    override fun hashCode(): Int = android.hashCode()

    override fun toString(): String = "DocumentSnapshot(id=$id, exists=$exists)"
}
