package dev.ynagai.firebase.firestore

import kotlinx.cinterop.ExperimentalForeignApi
import swiftPMImport.dev.ynagai.firebase.firebase.firestore.FIRAggregateField

@OptIn(ExperimentalForeignApi::class)
actual class AggregateField internal constructor(internal val apple: FIRAggregateField) {
    actual companion object {
        actual fun count(): AggregateField =
            AggregateField(FIRAggregateField.aggregateFieldForCount())

        actual fun sum(field: String): AggregateField =
            AggregateField(FIRAggregateField.aggregateFieldForSumOfField(field))

        actual fun average(field: String): AggregateField =
            AggregateField(FIRAggregateField.aggregateFieldForAverageOfField(field))
    }
}
