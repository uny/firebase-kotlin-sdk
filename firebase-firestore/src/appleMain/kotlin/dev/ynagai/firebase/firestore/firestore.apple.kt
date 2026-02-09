package dev.ynagai.firebase.firestore

import dev.ynagai.firebase.Firebase
import dev.ynagai.firebase.FirebaseApp
import kotlinx.cinterop.ExperimentalForeignApi
import swiftPMImport.dev.ynagai.firebase.firebase.firestore.FIRFirestore

@OptIn(ExperimentalForeignApi::class)
actual fun Firebase.firestore(app: FirebaseApp): FirebaseFirestore =
    FirebaseFirestore(FIRFirestore.firestoreForApp(app.apple))

@OptIn(ExperimentalForeignApi::class)
actual class FirebaseFirestore internal constructor(
    internal val apple: FIRFirestore,
) {
    actual fun collection(collectionPath: String): CollectionReference =
        CollectionReference(apple.collectionWithPath(collectionPath))

    actual fun document(documentPath: String): DocumentReference =
        DocumentReference(apple.documentWithPath(documentPath))

    actual fun collectionGroup(collectionId: String): Query =
        Query(apple.collectionGroupWithID(collectionId))

    actual fun batch(): WriteBatch = WriteBatch(apple.batch())

    actual suspend fun <T> runTransaction(func: Transaction.() -> T): T =
        awaitResult { callback ->
            apple.runTransactionWithBlock(
                { firTransaction, _ ->
                    func(Transaction(firTransaction ?: throw FirebaseFirestoreException("Transaction is unexpectedly null", FirestoreExceptionCode.INTERNAL)))
                },
                completion = { result, error ->
                    @Suppress("UNCHECKED_CAST")
                    callback(result as T?, error)
                },
            )
        }

    actual fun useEmulator(host: String, port: Int) {
        apple.useEmulatorWithHost(host, port.toLong())
    }

    actual suspend fun clearPersistence() {
        await { callback -> apple.clearPersistenceWithCompletion(callback) }
    }

    actual suspend fun disableNetwork() {
        await { callback -> apple.disableNetworkWithCompletion(callback) }
    }

    actual suspend fun enableNetwork() {
        await { callback -> apple.enableNetworkWithCompletion(callback) }
    }
}
