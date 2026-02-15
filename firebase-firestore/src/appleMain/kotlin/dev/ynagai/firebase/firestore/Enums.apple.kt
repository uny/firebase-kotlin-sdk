package dev.ynagai.firebase.firestore

import kotlinx.cinterop.ExperimentalForeignApi
import swiftPMImport.dev.ynagai.firebase.firebase.firestore.FIRAggregateSource
import swiftPMImport.dev.ynagai.firebase.firebase.firestore.FIRFirestoreSource

@OptIn(ExperimentalForeignApi::class)
@Suppress("DEPRECATION")
internal fun Source.toApple(): FIRFirestoreSource = when (this) {
    Source.DEFAULT -> FIRFirestoreSource.FIRFirestoreSourceDefault
    Source.SERVER -> FIRFirestoreSource.FIRFirestoreSourceServer
    Source.CACHE -> FIRFirestoreSource.FIRFirestoreSourceCache
}

@OptIn(ExperimentalForeignApi::class)
@Suppress("DEPRECATION")
internal fun AggregateSource.toApple(): FIRAggregateSource = when (this) {
    AggregateSource.SERVER -> FIRAggregateSource.FIRAggregateSourceServer
}
