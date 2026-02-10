package dev.ynagai.firebase.firestore

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.Foundation.NSError
import swiftPMImport.dev.ynagai.firebase.firebase.firestore.FIRFirestoreErrorCodeAborted
import swiftPMImport.dev.ynagai.firebase.firebase.firestore.FIRFirestoreErrorCodeAlreadyExists
import swiftPMImport.dev.ynagai.firebase.firebase.firestore.FIRFirestoreErrorCodeCancelled
import swiftPMImport.dev.ynagai.firebase.firebase.firestore.FIRFirestoreErrorCodeDataLoss
import swiftPMImport.dev.ynagai.firebase.firebase.firestore.FIRFirestoreErrorCodeDeadlineExceeded
import swiftPMImport.dev.ynagai.firebase.firebase.firestore.FIRFirestoreErrorCodeFailedPrecondition
import swiftPMImport.dev.ynagai.firebase.firebase.firestore.FIRFirestoreErrorCodeInternal
import swiftPMImport.dev.ynagai.firebase.firebase.firestore.FIRFirestoreErrorCodeInvalidArgument
import swiftPMImport.dev.ynagai.firebase.firebase.firestore.FIRFirestoreErrorCodeNotFound
import swiftPMImport.dev.ynagai.firebase.firebase.firestore.FIRFirestoreErrorCodeOK
import swiftPMImport.dev.ynagai.firebase.firebase.firestore.FIRFirestoreErrorCodeOutOfRange
import swiftPMImport.dev.ynagai.firebase.firebase.firestore.FIRFirestoreErrorCodePermissionDenied
import swiftPMImport.dev.ynagai.firebase.firebase.firestore.FIRFirestoreErrorCodeResourceExhausted
import swiftPMImport.dev.ynagai.firebase.firebase.firestore.FIRFirestoreErrorCodeUnauthenticated
import swiftPMImport.dev.ynagai.firebase.firebase.firestore.FIRFirestoreErrorCodeUnavailable
import swiftPMImport.dev.ynagai.firebase.firebase.firestore.FIRFirestoreErrorCodeUnimplemented
import swiftPMImport.dev.ynagai.firebase.firebase.firestore.FIRFirestoreErrorCodeUnknown
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@OptIn(ExperimentalForeignApi::class)
internal suspend fun <T : Any> awaitResult(block: (callback: (T?, NSError?) -> Unit) -> Unit): T =
    suspendCancellableCoroutine { continuation ->
        block { result, error ->
            when {
                error != null -> continuation.resumeWithException(error.toException())
                result != null -> continuation.resume(result)
                else -> continuation.resumeWithException(
                    FirebaseFirestoreException(
                        "Operation completed without error but returned null result.",
                        FirestoreExceptionCode.INTERNAL
                    )
                )
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

@OptIn(ExperimentalForeignApi::class)
internal fun NSError.toException(): FirebaseFirestoreException {
    val code = when (this.code) {
        FIRFirestoreErrorCodeOK -> FirestoreExceptionCode.OK
        FIRFirestoreErrorCodeCancelled -> FirestoreExceptionCode.CANCELLED
        FIRFirestoreErrorCodeUnknown -> FirestoreExceptionCode.UNKNOWN
        FIRFirestoreErrorCodeInvalidArgument -> FirestoreExceptionCode.INVALID_ARGUMENT
        FIRFirestoreErrorCodeDeadlineExceeded -> FirestoreExceptionCode.DEADLINE_EXCEEDED
        FIRFirestoreErrorCodeNotFound -> FirestoreExceptionCode.NOT_FOUND
        FIRFirestoreErrorCodeAlreadyExists -> FirestoreExceptionCode.ALREADY_EXISTS
        FIRFirestoreErrorCodePermissionDenied -> FirestoreExceptionCode.PERMISSION_DENIED
        FIRFirestoreErrorCodeResourceExhausted -> FirestoreExceptionCode.RESOURCE_EXHAUSTED
        FIRFirestoreErrorCodeFailedPrecondition -> FirestoreExceptionCode.FAILED_PRECONDITION
        FIRFirestoreErrorCodeAborted -> FirestoreExceptionCode.ABORTED
        FIRFirestoreErrorCodeOutOfRange -> FirestoreExceptionCode.OUT_OF_RANGE
        FIRFirestoreErrorCodeUnimplemented -> FirestoreExceptionCode.UNIMPLEMENTED
        FIRFirestoreErrorCodeInternal -> FirestoreExceptionCode.INTERNAL
        FIRFirestoreErrorCodeUnavailable -> FirestoreExceptionCode.UNAVAILABLE
        FIRFirestoreErrorCodeDataLoss -> FirestoreExceptionCode.DATA_LOSS
        FIRFirestoreErrorCodeUnauthenticated -> FirestoreExceptionCode.UNAUTHENTICATED
        else -> FirestoreExceptionCode.UNKNOWN
    }
    return FirebaseFirestoreException(
        message = localizedDescription,
        code = code,
    )
}
