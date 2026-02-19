package dev.ynagai.firebase.auth

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.Foundation.NSError
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@OptIn(ExperimentalForeignApi::class)
internal suspend fun <T : Any> awaitResult(block: (callback: (T?, NSError?) -> Unit) -> Unit): T =
    suspendCancellableCoroutine { continuation ->
        block { result, error ->
            when {
                error != null -> continuation.resumeWithException(error.toAuthException())
                result != null -> continuation.resume(result)
                else -> continuation.resumeWithException(
                    FirebaseAuthException(
                        "Operation completed without error but returned null result.",
                        FirebaseAuthExceptionCode.UNKNOWN,
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
                continuation.resumeWithException(error.toAuthException())
            } else {
                continuation.resume(Unit)
            }
        }
    }

// FIRAuthErrorCode raw values from Firebase iOS SDK AuthErrors.swift
private const val ERROR_INVALID_CREDENTIAL = 17004L
private const val ERROR_USER_DISABLED = 17005L
private const val ERROR_OPERATION_NOT_ALLOWED = 17006L
private const val ERROR_EMAIL_ALREADY_IN_USE = 17007L
private const val ERROR_INVALID_EMAIL = 17008L
private const val ERROR_WRONG_PASSWORD = 17009L
private const val ERROR_TOO_MANY_REQUESTS = 17010L
private const val ERROR_USER_NOT_FOUND = 17011L
private const val ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL = 17012L
private const val ERROR_REQUIRES_RECENT_LOGIN = 17014L
private const val ERROR_PROVIDER_ALREADY_LINKED = 17015L
private const val ERROR_INVALID_USER_TOKEN = 17017L
private const val ERROR_NETWORK_ERROR = 17020L
private const val ERROR_USER_TOKEN_EXPIRED = 17021L
private const val ERROR_USER_MISMATCH = 17024L
private const val ERROR_CREDENTIAL_ALREADY_IN_USE = 17025L
private const val ERROR_WEAK_PASSWORD = 17026L
private const val ERROR_EXPIRED_ACTION_CODE = 17029L
private const val ERROR_INVALID_ACTION_CODE = 17030L
private const val ERROR_MISSING_PHONE_NUMBER = 17041L
private const val ERROR_INVALID_PHONE_NUMBER = 17048L
private const val ERROR_INVALID_VERIFICATION_CODE = 17049L
private const val ERROR_INVALID_VERIFICATION_ID = 17050L
private const val ERROR_SESSION_EXPIRED = 17051L
private const val ERROR_QUOTA_EXCEEDED = 17052L

internal fun NSError.toAuthException(): FirebaseAuthException {
    val code = when (this.code) {
        ERROR_INVALID_EMAIL -> FirebaseAuthExceptionCode.INVALID_EMAIL
        ERROR_USER_DISABLED -> FirebaseAuthExceptionCode.USER_DISABLED
        ERROR_USER_NOT_FOUND -> FirebaseAuthExceptionCode.USER_NOT_FOUND
        ERROR_WRONG_PASSWORD -> FirebaseAuthExceptionCode.WRONG_PASSWORD
        ERROR_EMAIL_ALREADY_IN_USE -> FirebaseAuthExceptionCode.EMAIL_ALREADY_IN_USE
        ERROR_WEAK_PASSWORD -> FirebaseAuthExceptionCode.WEAK_PASSWORD
        ERROR_OPERATION_NOT_ALLOWED -> FirebaseAuthExceptionCode.OPERATION_NOT_ALLOWED
        ERROR_TOO_MANY_REQUESTS -> FirebaseAuthExceptionCode.TOO_MANY_REQUESTS
        ERROR_REQUIRES_RECENT_LOGIN -> FirebaseAuthExceptionCode.REQUIRES_RECENT_LOGIN
        ERROR_CREDENTIAL_ALREADY_IN_USE -> FirebaseAuthExceptionCode.CREDENTIAL_ALREADY_IN_USE
        ERROR_INVALID_CREDENTIAL -> FirebaseAuthExceptionCode.INVALID_CREDENTIAL
        ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL -> FirebaseAuthExceptionCode.ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL
        ERROR_USER_MISMATCH -> FirebaseAuthExceptionCode.USER_MISMATCH
        ERROR_PROVIDER_ALREADY_LINKED -> FirebaseAuthExceptionCode.PROVIDER_ALREADY_LINKED
        ERROR_EXPIRED_ACTION_CODE -> FirebaseAuthExceptionCode.EXPIRED_ACTION_CODE
        ERROR_INVALID_ACTION_CODE -> FirebaseAuthExceptionCode.INVALID_ACTION_CODE
        ERROR_MISSING_PHONE_NUMBER -> FirebaseAuthExceptionCode.MISSING_PHONE_NUMBER
        ERROR_INVALID_PHONE_NUMBER -> FirebaseAuthExceptionCode.INVALID_PHONE_NUMBER
        ERROR_INVALID_VERIFICATION_CODE -> FirebaseAuthExceptionCode.INVALID_VERIFICATION_CODE
        ERROR_INVALID_VERIFICATION_ID -> FirebaseAuthExceptionCode.INVALID_VERIFICATION_ID
        ERROR_SESSION_EXPIRED -> FirebaseAuthExceptionCode.SESSION_EXPIRED
        ERROR_QUOTA_EXCEEDED -> FirebaseAuthExceptionCode.QUOTA_EXCEEDED
        ERROR_NETWORK_ERROR -> FirebaseAuthExceptionCode.NETWORK_ERROR
        ERROR_USER_TOKEN_EXPIRED -> FirebaseAuthExceptionCode.USER_TOKEN_EXPIRED
        ERROR_INVALID_USER_TOKEN -> FirebaseAuthExceptionCode.INVALID_USER_TOKEN
        else -> FirebaseAuthExceptionCode.UNKNOWN
    }
    return FirebaseAuthException(
        message = localizedDescription,
        errorCode = code,
    )
}
