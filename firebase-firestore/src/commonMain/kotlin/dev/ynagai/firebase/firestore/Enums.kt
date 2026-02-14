package dev.ynagai.firebase.firestore

enum class Direction {
    ASCENDING,
    DESCENDING,
}

enum class Source {
    DEFAULT,
    CACHE,
    SERVER,
}

enum class MetadataChanges {
    EXCLUDE,
    INCLUDE,
}
