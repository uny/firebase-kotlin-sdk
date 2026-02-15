package dev.ynagai.firebase.firestore

import com.google.firebase.firestore.AggregateQuerySnapshot as AndroidAggregateQuerySnapshot

actual class AggregateQuerySnapshot internal constructor(
    internal val android: AndroidAggregateQuerySnapshot,
) {
    actual val count: Long
        get() = android.count

    actual fun get(field: AggregateField): Any? = android.get(field.android)
}
