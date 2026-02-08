package dev.ynagai.firebase.firestore

import com.google.firebase.firestore.QuerySnapshot as AndroidQuerySnapshot

actual class QuerySnapshot internal constructor(
    internal val android: AndroidQuerySnapshot,
) {
    actual val documents: List<DocumentSnapshot>
        get() = android.documents.map { DocumentSnapshot(it) }

    actual val isEmpty: Boolean
        get() = android.isEmpty

    actual val size: Int
        get() = android.size()
}
