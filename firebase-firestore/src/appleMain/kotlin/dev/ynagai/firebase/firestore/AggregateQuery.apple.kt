package dev.ynagai.firebase.firestore

import kotlinx.cinterop.ExperimentalForeignApi
import swiftPMImport.dev.ynagai.firebase.firebase.firestore.FIRAggregateQuery
import swiftPMImport.dev.ynagai.firebase.firebase.firestore.FIRAggregateQuerySnapshot

@OptIn(ExperimentalForeignApi::class)
actual class AggregateQuery internal constructor(
    internal val apple: FIRAggregateQuery,
    actual val query: Query,
) {
    actual suspend fun get(source: AggregateSource): AggregateQuerySnapshot {
        val result = awaitResult<FIRAggregateQuerySnapshot> { callback ->
            apple.aggregationWithSource(source.toApple(), completion = callback)
        }
        return AggregateQuerySnapshot(result)
    }
}
