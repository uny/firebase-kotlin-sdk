package dev.ynagai.firebase.firestore

import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.WriteBatch as AndroidWriteBatch
import kotlinx.coroutines.tasks.await

actual class WriteBatch internal constructor(
    internal val android: AndroidWriteBatch,
) {
    actual fun set(documentRef: DocumentReference, data: Map<String, Any?>, merge: Boolean): WriteBatch {
        if (merge) {
            android.set(documentRef.android, data.toAndroidData(), SetOptions.merge())
        } else {
            android.set(documentRef.android, data.toAndroidData())
        }
        return this
    }

    actual fun set(documentRef: DocumentReference, data: Map<String, Any?>, options: dev.ynagai.firebase.firestore.SetOptions): WriteBatch {
        when (options) {
            is dev.ynagai.firebase.firestore.SetOptions.Overwrite -> android.set(documentRef.android, data.toAndroidData())
            is dev.ynagai.firebase.firestore.SetOptions.Merge -> android.set(documentRef.android, data.toAndroidData(), SetOptions.merge())
            is dev.ynagai.firebase.firestore.SetOptions.MergeFields -> android.set(documentRef.android, data.toAndroidData(), SetOptions.mergeFields(options.fields))
        }
        return this
    }

    actual fun update(documentRef: DocumentReference, data: Map<String, Any?>): WriteBatch {
        android.update(documentRef.android, data.toAndroidData())
        return this
    }

    actual fun delete(documentRef: DocumentReference): WriteBatch {
        android.delete(documentRef.android)
        return this
    }

    actual suspend fun commit() {
        android.commit().await()
    }
}
