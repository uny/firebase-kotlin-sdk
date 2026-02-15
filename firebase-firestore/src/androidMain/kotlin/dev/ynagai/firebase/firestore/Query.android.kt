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

    actual fun whereEqualTo(fieldPath: FieldPath, value: Any?): Query =
        Query(android.whereEqualTo(fieldPath.android, value))

    actual fun whereNotEqualTo(fieldPath: FieldPath, value: Any?): Query =
        Query(android.whereNotEqualTo(fieldPath.android, value))

    actual fun whereLessThan(fieldPath: FieldPath, value: Any): Query =
        Query(android.whereLessThan(fieldPath.android, value))

    actual fun whereLessThanOrEqualTo(fieldPath: FieldPath, value: Any): Query =
        Query(android.whereLessThanOrEqualTo(fieldPath.android, value))

    actual fun whereGreaterThan(fieldPath: FieldPath, value: Any): Query =
        Query(android.whereGreaterThan(fieldPath.android, value))

    actual fun whereGreaterThanOrEqualTo(fieldPath: FieldPath, value: Any): Query =
        Query(android.whereGreaterThanOrEqualTo(fieldPath.android, value))

    actual fun whereArrayContains(fieldPath: FieldPath, value: Any): Query =
        Query(android.whereArrayContains(fieldPath.android, value))

    actual fun whereArrayContainsAny(fieldPath: FieldPath, values: List<Any>): Query =
        Query(android.whereArrayContainsAny(fieldPath.android, values))

    actual fun whereIn(fieldPath: FieldPath, values: List<Any>): Query =
        Query(android.whereIn(fieldPath.android, values))

    actual fun whereNotIn(fieldPath: FieldPath, values: List<Any>): Query =
        Query(android.whereNotIn(fieldPath.android, values))

    actual fun orderBy(fieldPath: FieldPath, direction: Direction): Query =
        Query(android.orderBy(fieldPath.android, direction.toAndroid()))

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

    actual fun count(): AggregateQuery =
        AggregateQuery(android.count(), this)

    actual fun aggregate(vararg fields: AggregateField): AggregateQuery {
        val androidFields = fields.map { it.android }
        return AggregateQuery(
            android.aggregate(androidFields.first(), *androidFields.drop(1).toTypedArray()),
            this,
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Query) return false
        return android == other.android
    }

    override fun hashCode(): Int = android.hashCode()

    override fun toString(): String = "Query(android=$android)"
}
