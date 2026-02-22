package dev.ynagai.firebase.firestore

import kotlinx.coroutines.flow.Flow

expect open class Query {
    fun whereEqualTo(field: String, value: Any?): Query
    fun whereNotEqualTo(field: String, value: Any?): Query
    fun whereLessThan(field: String, value: Any): Query
    fun whereLessThanOrEqualTo(field: String, value: Any): Query
    fun whereGreaterThan(field: String, value: Any): Query
    fun whereGreaterThanOrEqualTo(field: String, value: Any): Query
    fun whereArrayContains(field: String, value: Any): Query
    fun whereArrayContainsAny(field: String, values: List<Any>): Query
    fun whereIn(field: String, values: List<Any>): Query
    fun whereNotIn(field: String, values: List<Any>): Query
    fun orderBy(field: String, direction: Direction = Direction.ASCENDING): Query

    fun whereEqualTo(fieldPath: FieldPath, value: Any?): Query
    fun whereNotEqualTo(fieldPath: FieldPath, value: Any?): Query
    fun whereLessThan(fieldPath: FieldPath, value: Any): Query
    fun whereLessThanOrEqualTo(fieldPath: FieldPath, value: Any): Query
    fun whereGreaterThan(fieldPath: FieldPath, value: Any): Query
    fun whereGreaterThanOrEqualTo(fieldPath: FieldPath, value: Any): Query
    fun whereArrayContains(fieldPath: FieldPath, value: Any): Query
    fun whereArrayContainsAny(fieldPath: FieldPath, values: List<Any>): Query
    fun whereIn(fieldPath: FieldPath, values: List<Any>): Query
    fun whereNotIn(fieldPath: FieldPath, values: List<Any>): Query
    fun orderBy(fieldPath: FieldPath, direction: Direction = Direction.ASCENDING): Query

    fun where(filter: Filter): Query
    fun limit(limit: Long): Query
    fun limitToLast(limit: Long): Query
    fun startAt(vararg fieldValues: Any): Query
    fun startAt(snapshot: DocumentSnapshot): Query
    fun startAfter(vararg fieldValues: Any): Query
    fun startAfter(snapshot: DocumentSnapshot): Query
    fun endAt(vararg fieldValues: Any): Query
    fun endAt(snapshot: DocumentSnapshot): Query
    fun endBefore(vararg fieldValues: Any): Query
    fun endBefore(snapshot: DocumentSnapshot): Query
    suspend fun get(source: Source = Source.DEFAULT): QuerySnapshot
    val snapshots: Flow<QuerySnapshot>
    fun snapshots(metadataChanges: MetadataChanges): Flow<QuerySnapshot>
    fun count(): AggregateQuery
    fun aggregate(vararg fields: AggregateField): AggregateQuery
}
