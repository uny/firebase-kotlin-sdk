package dev.ynagai.firebase.firestore

import dev.ynagai.firebase.Firebase
import dev.ynagai.firebase.FirebaseApp
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.Foundation.NSError
import swiftPMImport.dev.ynagai.firebase.firebase.firestore.FIRFirestore
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@OptIn(ExperimentalForeignApi::class)
actual val Firebase.firestore: FirebaseFirestore
    get() = FirebaseFirestore(FIRFirestore.firestore())

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
        suspendCancellableCoroutine { continuation ->
            apple.runTransactionWithBlock(
                { firTransaction, _ ->
                    func(Transaction(firTransaction ?: throw FirebaseFirestoreException("Transaction is unexpectedly null", FirestoreExceptionCode.INTERNAL)))
                },
                completion = { result: Any?, error: NSError? ->
                    when {
                        error != null -> continuation.resumeWithException(error.toException())
                        else -> {
                            @Suppress("UNCHECKED_CAST")
                            continuation.resume(result as T)
                        }
                    }
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
