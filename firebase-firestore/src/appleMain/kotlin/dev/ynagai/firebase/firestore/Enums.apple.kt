package dev.ynagai.firebase.firestore

import kotlinx.cinterop.ExperimentalForeignApi
import swiftPMImport.dev.ynagai.firebase.firebase.firestore.FIRFirestoreSource

@OptIn(ExperimentalForeignApi::class)
@Suppress("DEPRECATION")
internal fun Source.toApple(): FIRFirestoreSource = when (this) {
    Source.DEFAULT -> FIRFirestoreSource.byValue(0uL) // FIRFirestoreSourceDefault
    Source.SERVER -> FIRFirestoreSource.byValue(1uL)  // FIRFirestoreSourceServer
    Source.CACHE -> FIRFirestoreSource.byValue(2uL)   // FIRFirestoreSourceCache
}
