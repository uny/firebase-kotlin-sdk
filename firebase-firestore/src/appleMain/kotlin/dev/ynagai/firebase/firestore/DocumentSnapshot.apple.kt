package dev.ynagai.firebase.firestore

import dev.ynagai.firebase.Timestamp
import kotlinx.cinterop.ExperimentalForeignApi
import swiftPMImport.dev.ynagai.firebase.firebase.firestore.FIRDocumentSnapshot

@OptIn(ExperimentalForeignApi::class)
actual class DocumentSnapshot internal constructor(
    internal val apple: FIRDocumentSnapshot,
) {
    actual val id: String
        get() = apple.documentID

    actual val reference: DocumentReference
        get() = DocumentReference(apple.reference)

    actual val exists: Boolean
        get() = apple.exists

    actual val metadata: SnapshotMetadata
        get() = apple.metadata.let {
            SnapshotMetadata(
                hasPendingWrites = it.hasPendingWrites(),
                isFromCache = it.isFromCache(),
            )
        }

    @Suppress("UNCHECKED_CAST")
    actual fun getData(): Map<String, Any?>? =
        apple.data() as? Map<String, Any?>

    actual fun get(field: String): Any? =
        apple.valueForField(field)

    actual fun getString(field: String): String? =
        apple.valueForField(field) as? String

    actual fun getLong(field: String): Long? =
        (apple.valueForField(field) as? Number)?.toLong()

    actual fun getDouble(field: String): Double? =
        (apple.valueForField(field) as? Number)?.toDouble()

    actual fun getBoolean(field: String): Boolean? =
        apple.valueForField(field) as? Boolean

    actual fun getTimestamp(field: String): Timestamp? {
        val value = apple.valueForField(field) ?: return null
        return nativeTimestampToKmp(value)
    }

    actual fun getGeoPoint(field: String): GeoPoint? {
        val value = apple.valueForField(field) ?: return null
        return nativeGeoPointToKmp(value)
    }

    actual fun getBlob(field: String): Blob? {
        val value = apple.valueForField(field) ?: return null
        return nativeBlobToKmp(value)
    }

    actual fun contains(field: String): Boolean = apple.valueForField(field) != null

    actual fun contains(fieldPath: FieldPath): Boolean = apple.valueForField(fieldPath.apple) != null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is DocumentSnapshot) return false
        return apple == other.apple
    }

    override fun hashCode(): Int = apple.hash.toInt()

    override fun toString(): String = "DocumentSnapshot(id=$id, exists=$exists)"
}
