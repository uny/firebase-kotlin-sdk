package dev.ynagai.firebase.firestore

import kotlinx.cinterop.ExperimentalForeignApi
import swiftPMImport.dev.ynagai.firebase.firebase.firestore.FIRFieldValue

@OptIn(ExperimentalForeignApi::class)
actual class FieldValue internal constructor(internal val apple: FIRFieldValue) {
    actual companion object {
        actual fun serverTimestamp(): FieldValue =
            FieldValue(FIRFieldValue.fieldValueForServerTimestamp())

        actual fun increment(value: Long): FieldValue =
            FieldValue(FIRFieldValue.fieldValueForIntegerIncrement(value))

        actual fun increment(value: Double): FieldValue =
            FieldValue(FIRFieldValue.fieldValueForDoubleIncrement(value))

        actual fun arrayUnion(vararg elements: Any): FieldValue =
            FieldValue(FIRFieldValue.fieldValueForArrayUnion(elements.toList()))

        actual fun arrayRemove(vararg elements: Any): FieldValue =
            FieldValue(FIRFieldValue.fieldValueForArrayRemove(elements.toList()))

        actual fun delete(): FieldValue =
            FieldValue(FIRFieldValue.fieldValueForDelete())
    }
}

internal fun Map<String, Any?>.toAppleData(): Map<Any?, *> =
    mapValues { (_, value) -> toAppleValue(value) }

@OptIn(ExperimentalForeignApi::class)
private fun toAppleValue(value: Any?): Any? = when (value) {
    is FieldValue -> value.apple
    is Map<*, *> -> {
        @Suppress("UNCHECKED_CAST")
        (value as Map<String, Any?>).toAppleData()
    }
    is List<*> -> value.map { toAppleValue(it) }
    else -> value
}
