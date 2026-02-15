package dev.ynagai.firebase.firestore

expect class AggregateField {
    companion object {
        fun count(): AggregateField
        fun sum(field: String): AggregateField
        fun average(field: String): AggregateField
    }
}
