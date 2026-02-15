package dev.ynagai.firebase.firestore

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
}
