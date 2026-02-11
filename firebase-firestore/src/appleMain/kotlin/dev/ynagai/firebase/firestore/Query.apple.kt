package dev.ynagai.firebase.firestore

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import swiftPMImport.dev.ynagai.firebase.firebase.firestore.FIRQuery
import swiftPMImport.dev.ynagai.firebase.firebase.firestore.FIRQuerySnapshot

@OptIn(ExperimentalForeignApi::class)
actual open class Query internal constructor(internal open val apple: FIRQuery) {
    actual fun whereEqualTo(field: String, value: Any?): Query =
        Query(apple.queryWhereField(field, isEqualTo = value ?: platform.Foundation.NSNull()))

    actual fun whereNotEqualTo(field: String, value: Any?): Query =
        Query(apple.queryWhereField(field, isNotEqualTo = value ?: platform.Foundation.NSNull()))

    actual fun whereLessThan(field: String, value: Any): Query =
        Query(apple.queryWhereField(field, isLessThan = value))

    actual fun whereLessThanOrEqualTo(field: String, value: Any): Query =
        Query(apple.queryWhereField(field, isLessThanOrEqualTo = value))

    actual fun whereGreaterThan(field: String, value: Any): Query =
        Query(apple.queryWhereField(field, isGreaterThan = value))

    actual fun whereGreaterThanOrEqualTo(field: String, value: Any): Query =
        Query(apple.queryWhereField(field, isGreaterThanOrEqualTo = value))

    actual fun whereArrayContains(field: String, value: Any): Query =
        Query(apple.queryWhereField(field, arrayContains = value))

    actual fun whereArrayContainsAny(field: String, values: List<Any>): Query =
        Query(apple.queryWhereField(field, arrayContainsAny = values))

    actual fun whereIn(field: String, values: List<Any>): Query =
        Query(apple.queryWhereField(field, `in` = values))

    actual fun whereNotIn(field: String, values: List<Any>): Query =
        Query(apple.queryWhereField(field, notIn = values))

    actual fun orderBy(field: String, direction: Direction): Query =
        Query(apple.queryOrderedByField(field, descending = direction == Direction.DESCENDING))

    actual fun limit(limit: Long): Query =
        Query(apple.queryLimitedTo(limit))

    actual fun startAt(vararg fieldValues: Any): Query =
        Query(apple.queryStartingAtValues(fieldValues.toList()))

    actual fun startAfter(vararg fieldValues: Any): Query =
        Query(apple.queryStartingAfterValues(fieldValues.toList()))

    actual fun endAt(vararg fieldValues: Any): Query =
        Query(apple.queryEndingAtValues(fieldValues.toList()))

    actual fun endBefore(vararg fieldValues: Any): Query =
        Query(apple.queryEndingBeforeValues(fieldValues.toList()))

    actual suspend fun get(source: Source): QuerySnapshot {
        val result = awaitResult<FIRQuerySnapshot> { callback ->
            apple.getDocumentsWithSource(source.toApple(), completion = callback)
        }
        return QuerySnapshot(result)
    }

    actual val snapshots: Flow<QuerySnapshot>
        get() = callbackFlow {
            val listener = apple.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error.toException())
                } else if (snapshot != null) {
                    trySend(QuerySnapshot(snapshot))
                }
            }
            awaitClose { listener.remove() }
        }

    actual fun snapshots(metadataChanges: MetadataChanges): Flow<QuerySnapshot> =
        callbackFlow {
            val listener = apple.addSnapshotListenerWithIncludeMetadataChanges(
                metadataChanges == MetadataChanges.INCLUDE
            ) { snapshot, error ->
                if (error != null) {
                    close(error.toException())
                } else if (snapshot != null) {
                    trySend(QuerySnapshot(snapshot))
                }
            }
            awaitClose { listener.remove() }
        }
}
