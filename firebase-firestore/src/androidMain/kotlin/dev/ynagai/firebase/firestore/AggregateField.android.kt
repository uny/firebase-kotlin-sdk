package dev.ynagai.firebase.firestore

import com.google.firebase.firestore.AggregateField as AndroidAggregateField

actual class AggregateField internal constructor(internal val android: AndroidAggregateField) {
    actual companion object {
        actual fun count(): AggregateField =
            AggregateField(AndroidAggregateField.count())

        actual fun sum(field: String): AggregateField =
            AggregateField(AndroidAggregateField.sum(field))

        actual fun average(field: String): AggregateField =
            AggregateField(AndroidAggregateField.average(field))
    }
}
