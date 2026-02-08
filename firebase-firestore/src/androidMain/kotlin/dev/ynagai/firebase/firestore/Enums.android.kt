package dev.ynagai.firebase.firestore

import com.google.firebase.firestore.Query as AndroidQuery

internal fun Direction.toAndroid(): AndroidQuery.Direction = when (this) {
    Direction.ASCENDING -> AndroidQuery.Direction.ASCENDING
    Direction.DESCENDING -> AndroidQuery.Direction.DESCENDING
}

internal fun com.google.firebase.firestore.Source.toCommon(): Source = when (this) {
    com.google.firebase.firestore.Source.DEFAULT -> Source.DEFAULT
    com.google.firebase.firestore.Source.CACHE -> Source.CACHE
    com.google.firebase.firestore.Source.SERVER -> Source.SERVER
}

internal fun Source.toAndroid(): com.google.firebase.firestore.Source = when (this) {
    Source.DEFAULT -> com.google.firebase.firestore.Source.DEFAULT
    Source.CACHE -> com.google.firebase.firestore.Source.CACHE
    Source.SERVER -> com.google.firebase.firestore.Source.SERVER
}
