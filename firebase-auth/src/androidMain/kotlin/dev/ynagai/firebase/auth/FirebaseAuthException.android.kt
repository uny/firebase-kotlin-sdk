package dev.ynagai.firebase.auth

import com.google.firebase.auth.FirebaseAuthException as AndroidFirebaseAuthException

internal fun AndroidFirebaseAuthException.toCommon(): FirebaseAuthException =
    FirebaseAuthException(
        message = message,
        errorCode = errorCode.toAuthExceptionCode(),
    )

internal fun String.toAuthExceptionCode(): FirebaseAuthExceptionCode = when (this) {
    "ERROR_INVALID_EMAIL" -> FirebaseAuthExceptionCode.INVALID_EMAIL
    "ERROR_USER_DISABLED" -> FirebaseAuthExceptionCode.USER_DISABLED
    "ERROR_USER_NOT_FOUND" -> FirebaseAuthExceptionCode.USER_NOT_FOUND
    "ERROR_WRONG_PASSWORD" -> FirebaseAuthExceptionCode.WRONG_PASSWORD
    "ERROR_EMAIL_ALREADY_IN_USE" -> FirebaseAuthExceptionCode.EMAIL_ALREADY_IN_USE
    "ERROR_WEAK_PASSWORD" -> FirebaseAuthExceptionCode.WEAK_PASSWORD
    "ERROR_OPERATION_NOT_ALLOWED" -> FirebaseAuthExceptionCode.OPERATION_NOT_ALLOWED
    "ERROR_TOO_MANY_REQUESTS" -> FirebaseAuthExceptionCode.TOO_MANY_REQUESTS
    "ERROR_REQUIRES_RECENT_LOGIN" -> FirebaseAuthExceptionCode.REQUIRES_RECENT_LOGIN
    "ERROR_CREDENTIAL_ALREADY_IN_USE" -> FirebaseAuthExceptionCode.CREDENTIAL_ALREADY_IN_USE
    "ERROR_INVALID_CREDENTIAL" -> FirebaseAuthExceptionCode.INVALID_CREDENTIAL
    "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL" -> FirebaseAuthExceptionCode.ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL
    "ERROR_USER_MISMATCH" -> FirebaseAuthExceptionCode.USER_MISMATCH
    "ERROR_PROVIDER_ALREADY_LINKED" -> FirebaseAuthExceptionCode.PROVIDER_ALREADY_LINKED
    "ERROR_EXPIRED_ACTION_CODE" -> FirebaseAuthExceptionCode.EXPIRED_ACTION_CODE
    "ERROR_INVALID_ACTION_CODE" -> FirebaseAuthExceptionCode.INVALID_ACTION_CODE
    "ERROR_NETWORK_REQUEST_FAILED" -> FirebaseAuthExceptionCode.NETWORK_ERROR
    "ERROR_USER_TOKEN_EXPIRED" -> FirebaseAuthExceptionCode.USER_TOKEN_EXPIRED
    "ERROR_INVALID_USER_TOKEN" -> FirebaseAuthExceptionCode.INVALID_USER_TOKEN
    else -> FirebaseAuthExceptionCode.UNKNOWN
}
