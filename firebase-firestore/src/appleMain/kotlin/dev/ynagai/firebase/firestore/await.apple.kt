package dev.ynagai.firebase.firestore

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.Foundation.NSError
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@OptIn(ExperimentalForeignApi::class)
internal suspend fun <T> awaitResult(block: (callback: (T?, NSError?) -> Unit) -> Unit): T =
    suspendCancellableCoroutine { continuation ->
        block { result, error ->
            if (error != null) {
                continuation.resumeWithException(error.toException())
            } else {
                @Suppress("UNCHECKED_CAST")
                continuation.resume(result as T)
            }
        }
    }

@OptIn(ExperimentalForeignApi::class)
internal suspend fun await(block: (callback: (NSError?) -> Unit) -> Unit): Unit =
    suspendCancellableCoroutine { continuation ->
        block { error ->
            if (error != null) {
                continuation.resumeWithException(error.toException())
            } else {
                continuation.resume(Unit)
            }
        }
    }

internal fun NSError.toException(): FirebaseFirestoreException {
    val code = when (this.code.toInt()) {
        0 -> FirestoreExceptionCode.OK
        1 -> FirestoreExceptionCode.CANCELLED
        2 -> FirestoreExceptionCode.UNKNOWN
        3 -> FirestoreExceptionCode.INVALID_ARGUMENT
        4 -> FirestoreExceptionCode.DEADLINE_EXCEEDED
        5 -> FirestoreExceptionCode.NOT_FOUND
        6 -> FirestoreExceptionCode.ALREADY_EXISTS
        7 -> FirestoreExceptionCode.PERMISSION_DENIED
        8 -> FirestoreExceptionCode.RESOURCE_EXHAUSTED
        9 -> FirestoreExceptionCode.FAILED_PRECONDITION
        10 -> FirestoreExceptionCode.ABORTED
        11 -> FirestoreExceptionCode.OUT_OF_RANGE
        12 -> FirestoreExceptionCode.UNIMPLEMENTED
        13 -> FirestoreExceptionCode.INTERNAL
        14 -> FirestoreExceptionCode.UNAVAILABLE
        15 -> FirestoreExceptionCode.DATA_LOSS
        16 -> FirestoreExceptionCode.UNAUTHENTICATED
        else -> FirestoreExceptionCode.UNKNOWN
    }
    return FirebaseFirestoreException(
        message = localizedDescription,
        code = code,
    )
}
