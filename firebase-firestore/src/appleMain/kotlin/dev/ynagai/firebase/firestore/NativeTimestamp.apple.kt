package dev.ynagai.firebase.firestore

import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.toKString
import platform.Foundation.NSNumber
import platform.Foundation.valueForKey
import platform.darwin.NSObject
import platform.objc.class_getName
import platform.objc.object_getClass

/**
 * Converts an ObjC FIRTimestamp (from FirebaseCore) to the KMP [Timestamp].
 *
 * FIRTimestamp is not directly importable via cinterop (it's a forward-declared class
 * from the FirebaseCore module), so we use ObjC runtime introspection and KVC to
 * extract its `seconds` and `nanoseconds` properties.
 *
 * @return The converted [Timestamp], or null if the value is not a FIRTimestamp.
 */
@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
internal fun nativeTimestampToKmp(value: Any): Timestamp? {
    val nsObj = value as? NSObject ?: return null
    val cls = object_getClass(nsObj) ?: return null
    val className = class_getName(cls)?.toKString() ?: return null
    if (className != "FIRTimestamp") return null
    val seconds = (nsObj.valueForKey("seconds") as? NSNumber)?.longLongValue ?: return null
    val nanoseconds = (nsObj.valueForKey("nanoseconds") as? NSNumber)?.intValue ?: return null
    return Timestamp(seconds = seconds, nanoseconds = nanoseconds)
}
