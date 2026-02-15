package dev.ynagai.firebase.firestore

expect class AggregateQuery {
    val query: Query
    suspend fun get(source: AggregateSource = AggregateSource.SERVER): AggregateQuerySnapshot
}
