package dev.ynagai.firebase.firestore

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.Foundation.NSData
import platform.posix.memcpy

/**
 * Converts an NSData instance (Firestore's Blob representation on iOS) to the KMP [Blob].
 *
 * Unlike FIRTimestamp or FIRGeoPoint, NSData is directly available via cinterop,
 * so no ObjC runtime introspection is needed.
 *
 * @return The converted [Blob], or null if the value is not an NSData.
 */
@OptIn(ExperimentalForeignApi::class)
internal fun nativeBlobToKmp(value: Any): Blob? {
    val nsData = value as? NSData ?: return null
    val bytes = nsData.toByteArray()
    return Blob(bytes)
}

@OptIn(ExperimentalForeignApi::class)
private fun NSData.toByteArray(): ByteArray {
    val length = length.toInt()
    if (length == 0) return ByteArray(0)
    val bytes = ByteArray(length)
    bytes.usePinned { pinned ->
        memcpy(pinned.addressOf(0), this.bytes, this.length)
    }
    return bytes
}
