package dev.ynagai.firebase.firestore

import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.Transaction as AndroidTransaction

actual class Transaction internal constructor(
    internal val android: AndroidTransaction,
) {
    actual fun get(documentRef: DocumentReference): DocumentSnapshot =
        DocumentSnapshot(android.get(documentRef.android))

    actual fun set(documentRef: DocumentReference, data: Map<String, Any?>, merge: Boolean): Transaction {
        if (merge) {
            android.set(documentRef.android, data.toAndroidData(), SetOptions.merge())
        } else {
            android.set(documentRef.android, data.toAndroidData())
        }
        return this
    }

    actual fun set(documentRef: DocumentReference, data: Map<String, Any?>, options: dev.ynagai.firebase.firestore.SetOptions): Transaction {
        when (options) {
            is dev.ynagai.firebase.firestore.SetOptions.Overwrite -> android.set(documentRef.android, data.toAndroidData())
            is dev.ynagai.firebase.firestore.SetOptions.Merge -> android.set(documentRef.android, data.toAndroidData(), SetOptions.merge())
            is dev.ynagai.firebase.firestore.SetOptions.MergeFields -> android.set(documentRef.android, data.toAndroidData(), SetOptions.mergeFields(options.fields))
        }
        return this
    }

    actual fun update(documentRef: DocumentReference, data: Map<String, Any?>): Transaction {
        android.update(documentRef.android, data.toAndroidData())
        return this
    }

    actual fun delete(documentRef: DocumentReference): Transaction {
        android.delete(documentRef.android)
        return this
    }
}
