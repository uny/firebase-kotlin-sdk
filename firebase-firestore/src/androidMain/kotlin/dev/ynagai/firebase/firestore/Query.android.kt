package dev.ynagai.firebase.firestore

import com.google.firebase.firestore.Query as AndroidQuery
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

actual open class Query internal constructor(internal open val android: AndroidQuery) {
    actual fun whereEqualTo(field: String, value: Any?): Query =
        Query(android.whereEqualTo(field, value))

    actual fun whereNotEqualTo(field: String, value: Any?): Query =
        Query(android.whereNotEqualTo(field, value))

    actual fun whereLessThan(field: String, value: Any): Query =
        Query(android.whereLessThan(field, value))

    actual fun whereLessThanOrEqualTo(field: String, value: Any): Query =
        Query(android.whereLessThanOrEqualTo(field, value))

    actual fun whereGreaterThan(field: String, value: Any): Query =
        Query(android.whereGreaterThan(field, value))

    actual fun whereGreaterThanOrEqualTo(field: String, value: Any): Query =
        Query(android.whereGreaterThanOrEqualTo(field, value))

    actual fun whereArrayContains(field: String, value: Any): Query =
        Query(android.whereArrayContains(field, value))

    actual fun whereArrayContainsAny(field: String, values: List<Any>): Query =
        Query(android.whereArrayContainsAny(field, values))

    actual fun whereIn(field: String, values: List<Any>): Query =
        Query(android.whereIn(field, values))

    actual fun whereNotIn(field: String, values: List<Any>): Query =
        Query(android.whereNotIn(field, values))

    actual fun orderBy(field: String, direction: Direction): Query =
        Query(android.orderBy(field, direction.toAndroid()))

    actual fun limit(limit: Long): Query =
        Query(android.limit(limit))

    actual fun startAt(vararg fieldValues: Any): Query =
        Query(android.startAt(*fieldValues))

    actual fun startAfter(vararg fieldValues: Any): Query =
        Query(android.startAfter(*fieldValues))

    actual fun endAt(vararg fieldValues: Any): Query =
        Query(android.endAt(*fieldValues))

    actual fun endBefore(vararg fieldValues: Any): Query =
        Query(android.endBefore(*fieldValues))

    actual suspend fun get(source: Source): QuerySnapshot =
        QuerySnapshot(android.get(source.toAndroid()).await())

    actual val snapshots: Flow<QuerySnapshot>
        get() = callbackFlow {
            val listener = android.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error.toCommon())
                } else if (snapshot != null) {
                    trySend(QuerySnapshot(snapshot))
                }
            }
            awaitClose { listener.remove() }
        }

    actual fun snapshots(metadataChanges: MetadataChanges): Flow<QuerySnapshot> =
        callbackFlow {
            val listener = android.addSnapshotListener(metadataChanges.toAndroid()) { snapshot, error ->
                if (error != null) {
                    close(error.toCommon())
                } else if (snapshot != null) {
                    trySend(QuerySnapshot(snapshot))
                }
            }
            awaitClose { listener.remove() }
        }
}
