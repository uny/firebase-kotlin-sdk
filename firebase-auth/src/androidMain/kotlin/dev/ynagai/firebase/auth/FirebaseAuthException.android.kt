package dev.ynagai.firebase.auth

import com.google.firebase.auth.FirebaseAuthException as AndroidFirebaseAuthException

internal fun AndroidFirebaseAuthException.toCommon(): FirebaseAuthException =
    FirebaseAuthException(
        message = message,
        code = errorCode.toAuthExceptionCode(),
    )

internal fun String.toAuthExceptionCode(): AuthExceptionCode = when (this) {
    "ERROR_INVALID_EMAIL" -> AuthExceptionCode.INVALID_EMAIL
    "ERROR_USER_DISABLED" -> AuthExceptionCode.USER_DISABLED
    "ERROR_USER_NOT_FOUND" -> AuthExceptionCode.USER_NOT_FOUND
    "ERROR_WRONG_PASSWORD" -> AuthExceptionCode.WRONG_PASSWORD
    "ERROR_EMAIL_ALREADY_IN_USE" -> AuthExceptionCode.EMAIL_ALREADY_IN_USE
    "ERROR_WEAK_PASSWORD" -> AuthExceptionCode.WEAK_PASSWORD
    "ERROR_OPERATION_NOT_ALLOWED" -> AuthExceptionCode.OPERATION_NOT_ALLOWED
    "ERROR_TOO_MANY_REQUESTS" -> AuthExceptionCode.TOO_MANY_REQUESTS
    "ERROR_REQUIRES_RECENT_LOGIN" -> AuthExceptionCode.REQUIRES_RECENT_LOGIN
    "ERROR_CREDENTIAL_ALREADY_IN_USE" -> AuthExceptionCode.CREDENTIAL_ALREADY_IN_USE
    "ERROR_INVALID_CREDENTIAL" -> AuthExceptionCode.INVALID_CREDENTIAL
    "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL" -> AuthExceptionCode.ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL
    "ERROR_USER_MISMATCH" -> AuthExceptionCode.USER_MISMATCH
    "ERROR_PROVIDER_ALREADY_LINKED" -> AuthExceptionCode.PROVIDER_ALREADY_LINKED
    "ERROR_EXPIRED_ACTION_CODE" -> AuthExceptionCode.EXPIRED_ACTION_CODE
    "ERROR_INVALID_ACTION_CODE" -> AuthExceptionCode.INVALID_ACTION_CODE
    "ERROR_NETWORK_REQUEST_FAILED" -> AuthExceptionCode.NETWORK_ERROR
    "ERROR_USER_TOKEN_EXPIRED" -> AuthExceptionCode.USER_TOKEN_EXPIRED
    "ERROR_INVALID_USER_TOKEN" -> AuthExceptionCode.INVALID_USER_TOKEN
    else -> AuthExceptionCode.UNKNOWN
}
