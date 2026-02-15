package dev.ynagai.firebase.firestore

import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.Foundation.NSData
import platform.Foundation.create
import swiftPMImport.dev.ynagai.firebase.firebase.firestore.FIRFieldValue
import swiftPMImport.dev.ynagai.firebase.firebase.firestore.FIRGeoPoint

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
    is GeoPoint -> FIRGeoPoint(latitude = value.latitude, longitude = value.longitude)
    is Blob -> value.toBytes().toNSData()
    is Map<*, *> -> {
        @Suppress("UNCHECKED_CAST")
        (value as Map<String, Any?>).toAppleData()
    }
    is List<*> -> value.map { toAppleValue(it) }
    else -> value
}

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
private fun ByteArray.toNSData(): NSData {
    if (isEmpty()) return NSData()
    return usePinned { pinned ->
        NSData.create(bytes = pinned.addressOf(0), length = size.toULong())
    }
}
