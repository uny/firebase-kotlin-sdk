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
 * Converts an ObjC FIRGeoPoint (from FirebaseFirestore) to the KMP [GeoPoint].
 *
 * FIRGeoPoint is not directly importable via cinterop (it's a forward-declared class),
 * so we use ObjC runtime introspection and KVC to extract its `latitude` and `longitude`
 * properties.
 *
 * @return The converted [GeoPoint], or null if the value is not a FIRGeoPoint.
 */
@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
internal fun nativeGeoPointToKmp(value: Any): GeoPoint? {
    val nsObj = value as? NSObject ?: return null
    val cls = object_getClass(nsObj) ?: return null
    val className = class_getName(cls)?.toKString() ?: return null
    if (className != "FIRGeoPoint") return null
    val latitude = (nsObj.valueForKey("latitude") as? NSNumber)?.doubleValue ?: return null
    val longitude = (nsObj.valueForKey("longitude") as? NSNumber)?.doubleValue ?: return null
    return GeoPoint(latitude = latitude, longitude = longitude)
}
