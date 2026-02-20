package dev.ynagai.firebase.auth

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.Foundation.NSError
import swiftPMImport.dev.ynagai.firebase.firebase.auth.FIRMultiFactorResolver
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
private const val ERROR_INVALID_CUSTOM_TOKEN = 17000L
private const val ERROR_CUSTOM_TOKEN_MISMATCH = 17002L
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
private const val ERROR_NO_SUCH_PROVIDER = 17016L
private const val ERROR_INVALID_USER_TOKEN = 17017L
private const val ERROR_NETWORK_ERROR = 17020L
private const val ERROR_USER_TOKEN_EXPIRED = 17021L
private const val ERROR_INVALID_API_KEY = 17023L
private const val ERROR_USER_MISMATCH = 17024L
private const val ERROR_CREDENTIAL_ALREADY_IN_USE = 17025L
private const val ERROR_WEAK_PASSWORD = 17026L
private const val ERROR_APP_NOT_AUTHORIZED = 17028L
private const val ERROR_EXPIRED_ACTION_CODE = 17029L
private const val ERROR_INVALID_ACTION_CODE = 17030L
private const val ERROR_INVALID_MESSAGE_PAYLOAD = 17031L
private const val ERROR_INVALID_SENDER = 17032L
private const val ERROR_INVALID_RECIPIENT_EMAIL = 17033L
private const val ERROR_MISSING_EMAIL = 17034L
private const val ERROR_UNAUTHORIZED_DOMAIN = 17038L
private const val ERROR_INVALID_CONTINUE_URI = 17039L
private const val ERROR_MISSING_CONTINUE_URI = 17040L
private const val ERROR_MISSING_PHONE_NUMBER = 17041L
private const val ERROR_INVALID_PHONE_NUMBER = 17042L
private const val ERROR_MISSING_VERIFICATION_CODE = 17043L
private const val ERROR_INVALID_VERIFICATION_CODE = 17044L
private const val ERROR_MISSING_VERIFICATION_ID = 17045L
private const val ERROR_INVALID_VERIFICATION_ID = 17046L
private const val ERROR_SESSION_EXPIRED = 17051L
private const val ERROR_QUOTA_EXCEEDED = 17052L
private const val ERROR_SECOND_FACTOR_REQUIRED = 17078L
private const val ERROR_MISSING_MULTI_FACTOR_SESSION = 17081L
private const val ERROR_MISSING_MULTI_FACTOR_INFO = 17082L
private const val ERROR_INVALID_MULTI_FACTOR_SESSION = 17083L
private const val ERROR_MULTI_FACTOR_INFO_NOT_FOUND = 17084L
private const val ERROR_UNSUPPORTED_FIRST_FACTOR = 17098L
private const val ERROR_MAXIMUM_SECOND_FACTOR_COUNT_EXCEEDED = 17099L
private const val ERROR_SECOND_FACTOR_ALREADY_ENROLLED = 17100L
private const val ERROR_REJECTED_CREDENTIAL = 17075L
private const val ERROR_INTERNAL_ERROR = 17999L

@OptIn(ExperimentalForeignApi::class)
internal fun NSError.toAuthException(): FirebaseAuthException {
    if (this.code == ERROR_SECOND_FACTOR_REQUIRED) {
        val resolver = this.userInfo["FIRAuthErrorUserInfoMultiFactorResolverKey"] as? FIRMultiFactorResolver
        if (resolver != null) {
            return FirebaseAuthMultiFactorException(
                message = localizedDescription,
                errorCode = FirebaseAuthExceptionCode.MULTI_FACTOR_AUTH_REQUIRED,
                resolver = MultiFactorResolver(resolver),
            )
        }
    }

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
        ERROR_INVALID_CUSTOM_TOKEN -> FirebaseAuthExceptionCode.INVALID_CUSTOM_TOKEN
        ERROR_CUSTOM_TOKEN_MISMATCH -> FirebaseAuthExceptionCode.CUSTOM_TOKEN_MISMATCH
        ERROR_NO_SUCH_PROVIDER -> FirebaseAuthExceptionCode.NO_SUCH_PROVIDER
        ERROR_INVALID_API_KEY -> FirebaseAuthExceptionCode.INVALID_API_KEY
        ERROR_APP_NOT_AUTHORIZED -> FirebaseAuthExceptionCode.APP_NOT_AUTHORIZED
        ERROR_INVALID_MESSAGE_PAYLOAD -> FirebaseAuthExceptionCode.INVALID_MESSAGE_PAYLOAD
        ERROR_INVALID_SENDER -> FirebaseAuthExceptionCode.INVALID_SENDER
        ERROR_INVALID_RECIPIENT_EMAIL -> FirebaseAuthExceptionCode.INVALID_RECIPIENT_EMAIL
        ERROR_MISSING_EMAIL -> FirebaseAuthExceptionCode.MISSING_EMAIL
        ERROR_MISSING_VERIFICATION_CODE -> FirebaseAuthExceptionCode.MISSING_VERIFICATION_CODE
        ERROR_MISSING_VERIFICATION_ID -> FirebaseAuthExceptionCode.MISSING_VERIFICATION_ID
        ERROR_UNAUTHORIZED_DOMAIN -> FirebaseAuthExceptionCode.UNAUTHORIZED_DOMAIN
        ERROR_INVALID_CONTINUE_URI -> FirebaseAuthExceptionCode.INVALID_CONTINUE_URI
        ERROR_MISSING_CONTINUE_URI -> FirebaseAuthExceptionCode.MISSING_CONTINUE_URI
        ERROR_REJECTED_CREDENTIAL -> FirebaseAuthExceptionCode.REJECTED_CREDENTIAL
        ERROR_SECOND_FACTOR_REQUIRED -> FirebaseAuthExceptionCode.MULTI_FACTOR_AUTH_REQUIRED
        ERROR_MISSING_MULTI_FACTOR_SESSION -> FirebaseAuthExceptionCode.MISSING_MULTI_FACTOR_SESSION
        ERROR_MISSING_MULTI_FACTOR_INFO -> FirebaseAuthExceptionCode.MISSING_MULTI_FACTOR_INFO
        ERROR_INVALID_MULTI_FACTOR_SESSION -> FirebaseAuthExceptionCode.INVALID_MULTI_FACTOR_SESSION
        ERROR_MULTI_FACTOR_INFO_NOT_FOUND -> FirebaseAuthExceptionCode.MULTI_FACTOR_INFO_NOT_FOUND
        ERROR_UNSUPPORTED_FIRST_FACTOR -> FirebaseAuthExceptionCode.UNSUPPORTED_FIRST_FACTOR
        ERROR_MAXIMUM_SECOND_FACTOR_COUNT_EXCEEDED -> FirebaseAuthExceptionCode.MAXIMUM_SECOND_FACTOR_COUNT_EXCEEDED
        ERROR_SECOND_FACTOR_ALREADY_ENROLLED -> FirebaseAuthExceptionCode.SECOND_FACTOR_ALREADY_ENROLLED
        ERROR_INTERNAL_ERROR -> FirebaseAuthExceptionCode.INTERNAL_ERROR
        else -> FirebaseAuthExceptionCode.UNKNOWN
    }
    return FirebaseAuthException(
        message = localizedDescription,
        errorCode = code,
    )
}
