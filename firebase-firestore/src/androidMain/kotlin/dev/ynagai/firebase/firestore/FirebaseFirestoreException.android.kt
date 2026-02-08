package dev.ynagai.firebase.firestore

import com.google.firebase.firestore.FirebaseFirestoreException as AndroidFirebaseFirestoreException

internal fun AndroidFirebaseFirestoreException.toCommon(): FirebaseFirestoreException =
    FirebaseFirestoreException(
        message = message,
        code = code.toCommon(),
    )

internal fun AndroidFirebaseFirestoreException.Code.toCommon(): FirestoreExceptionCode = when (this) {
    AndroidFirebaseFirestoreException.Code.OK -> FirestoreExceptionCode.OK
    AndroidFirebaseFirestoreException.Code.CANCELLED -> FirestoreExceptionCode.CANCELLED
    AndroidFirebaseFirestoreException.Code.UNKNOWN -> FirestoreExceptionCode.UNKNOWN
    AndroidFirebaseFirestoreException.Code.INVALID_ARGUMENT -> FirestoreExceptionCode.INVALID_ARGUMENT
    AndroidFirebaseFirestoreException.Code.DEADLINE_EXCEEDED -> FirestoreExceptionCode.DEADLINE_EXCEEDED
    AndroidFirebaseFirestoreException.Code.NOT_FOUND -> FirestoreExceptionCode.NOT_FOUND
    AndroidFirebaseFirestoreException.Code.ALREADY_EXISTS -> FirestoreExceptionCode.ALREADY_EXISTS
    AndroidFirebaseFirestoreException.Code.PERMISSION_DENIED -> FirestoreExceptionCode.PERMISSION_DENIED
    AndroidFirebaseFirestoreException.Code.RESOURCE_EXHAUSTED -> FirestoreExceptionCode.RESOURCE_EXHAUSTED
    AndroidFirebaseFirestoreException.Code.FAILED_PRECONDITION -> FirestoreExceptionCode.FAILED_PRECONDITION
    AndroidFirebaseFirestoreException.Code.ABORTED -> FirestoreExceptionCode.ABORTED
    AndroidFirebaseFirestoreException.Code.OUT_OF_RANGE -> FirestoreExceptionCode.OUT_OF_RANGE
    AndroidFirebaseFirestoreException.Code.UNIMPLEMENTED -> FirestoreExceptionCode.UNIMPLEMENTED
    AndroidFirebaseFirestoreException.Code.INTERNAL -> FirestoreExceptionCode.INTERNAL
    AndroidFirebaseFirestoreException.Code.UNAVAILABLE -> FirestoreExceptionCode.UNAVAILABLE
    AndroidFirebaseFirestoreException.Code.DATA_LOSS -> FirestoreExceptionCode.DATA_LOSS
    AndroidFirebaseFirestoreException.Code.UNAUTHENTICATED -> FirestoreExceptionCode.UNAUTHENTICATED
}
