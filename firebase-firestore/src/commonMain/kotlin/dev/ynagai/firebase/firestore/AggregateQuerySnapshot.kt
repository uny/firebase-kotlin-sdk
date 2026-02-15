package dev.ynagai.firebase.firestore

expect class AggregateQuerySnapshot {
    val count: Long
    fun get(field: AggregateField): Any?
}
