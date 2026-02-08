package dev.ynagai.firebase.firestore

expect class QuerySnapshot {
    val documents: List<DocumentSnapshot>
    val isEmpty: Boolean
    val size: Int
}
