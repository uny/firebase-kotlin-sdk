package dev.ynagai.firebase.firestore

import com.google.firebase.firestore.AggregateQuery as AndroidAggregateQuery
import kotlinx.coroutines.tasks.await

actual class AggregateQuery internal constructor(
    internal val android: AndroidAggregateQuery,
    actual val query: Query,
) {
    actual suspend fun get(source: AggregateSource): AggregateQuerySnapshot =
        AggregateQuerySnapshot(android.get(source.toAndroid()).await())
}
