package dev.ynagai.firebase.firestore

import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSNumber
import swiftPMImport.dev.ynagai.firebase.firebase.firestore.FIRAggregateQuerySnapshot

@OptIn(ExperimentalForeignApi::class)
actual class AggregateQuerySnapshot internal constructor(
    internal val apple: FIRAggregateQuerySnapshot,
) {
    actual val count: Long
        get() = (apple.count as NSNumber).longValue

    actual fun get(field: AggregateField): Any? =
        apple.valueForAggregateField(field.apple)
}
