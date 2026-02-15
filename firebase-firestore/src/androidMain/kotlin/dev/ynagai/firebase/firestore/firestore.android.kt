package dev.ynagai.firebase.firestore

import com.google.firebase.firestore.FirebaseFirestore as AndroidFirebaseFirestore
import dev.ynagai.firebase.Firebase
import dev.ynagai.firebase.FirebaseApp
import kotlinx.coroutines.tasks.await

actual val Firebase.firestore: FirebaseFirestore
    get() = FirebaseFirestore(AndroidFirebaseFirestore.getInstance())

actual fun Firebase.firestore(app: FirebaseApp): FirebaseFirestore =
    FirebaseFirestore(AndroidFirebaseFirestore.getInstance(app.android))

actual class FirebaseFirestore internal constructor(
    internal val android: AndroidFirebaseFirestore,
) {
    @Suppress("DEPRECATION")
    actual var settings: FirebaseFirestoreSettings
        get() = android.firestoreSettings.toCommon()
        set(value) {
            android.firestoreSettings = value.toAndroid()
        }

    actual fun collection(collectionPath: String): CollectionReference =
        CollectionReference(android.collection(collectionPath))

    actual fun document(documentPath: String): DocumentReference =
        DocumentReference(android.document(documentPath))

    actual fun collectionGroup(collectionId: String): Query =
        Query(android.collectionGroup(collectionId))

    actual fun batch(): WriteBatch = WriteBatch(android.batch())

    actual suspend fun <T> runTransaction(func: Transaction.() -> T): T =
        android.runTransaction<T> { func(Transaction(it)) }.await()

    actual fun useEmulator(host: String, port: Int) {
        android.useEmulator(host, port)
    }

    actual suspend fun clearPersistence() {
        android.clearPersistence().await()
    }

    actual suspend fun disableNetwork() {
        android.disableNetwork().await()
    }

    actual suspend fun enableNetwork() {
        android.enableNetwork().await()
    }
}
