package dev.ynagai.firebase.firestore

import com.google.firebase.firestore.FieldValue as AndroidFieldValue

actual class FieldValue internal constructor(internal val android: AndroidFieldValue) {
    actual companion object {
        actual fun serverTimestamp(): FieldValue = FieldValue(AndroidFieldValue.serverTimestamp())
        actual fun increment(value: Long): FieldValue = FieldValue(AndroidFieldValue.increment(value))
        actual fun increment(value: Double): FieldValue = FieldValue(AndroidFieldValue.increment(value))
        actual fun arrayUnion(vararg elements: Any): FieldValue = FieldValue(AndroidFieldValue.arrayUnion(*elements))
        actual fun arrayRemove(vararg elements: Any): FieldValue = FieldValue(AndroidFieldValue.arrayRemove(*elements))
        actual fun delete(): FieldValue = FieldValue(AndroidFieldValue.delete())
    }
}

internal fun Map<String, Any?>.toAndroidData(): Map<String, Any?> =
    mapValues { (_, value) ->
        when (value) {
            is FieldValue -> value.android
            is Map<*, *> -> {
                @Suppress("UNCHECKED_CAST")
                (value as Map<String, Any?>).toAndroidData()
            }
            is List<*> -> value.map { if (it is FieldValue) it.android else it }
            else -> value
        }
    }
