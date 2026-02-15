package dev.ynagai.firebase.firestore

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
}
