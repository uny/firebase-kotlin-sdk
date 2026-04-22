package dev.ynagai.firebase.ai

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.Foundation.NSError
import platform.Foundation.NSNumber
import platform.Foundation.NSUnderlyingErrorKey
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@OptIn(ExperimentalForeignApi::class)
internal suspend fun <T : Any> awaitResult(block: (callback: (T?, NSError?) -> Unit) -> Unit): T =
    suspendCancellableCoroutine { continuation ->
        block { result, error ->
            when {
                error != null -> continuation.resumeWithException(
                    error.toFirebaseAIException()
                )
                result != null -> continuation.resume(result)
                else -> continuation.resumeWithException(
                    FirebaseAIException("Operation completed without error but returned null result.")
                )
            }
        }
    }

@OptIn(ExperimentalForeignApi::class)
internal suspend fun await(block: (callback: (NSError?) -> Unit) -> Unit): Unit =
    suspendCancellableCoroutine { continuation ->
        block { error ->
            if (error != null) {
                continuation.resumeWithException(error.toFirebaseAIException())
            } else {
                continuation.resume(Unit)
            }
        }
    }

@OptIn(ExperimentalForeignApi::class)
internal suspend fun awaitVoid(block: (callback: () -> Unit) -> Unit): Unit =
    suspendCancellableCoroutine { continuation ->
        block {
            continuation.resume(Unit)
        }
    }

@OptIn(ExperimentalForeignApi::class)
internal suspend fun <T : Any> awaitNullableResult(block: (callback: (T?, NSError?) -> Unit) -> Unit): T? =
    suspendCancellableCoroutine { continuation ->
        block { result, error ->
            when {
                error != null -> continuation.resumeWithException(
                    error.toFirebaseAIException()
                )
                else -> continuation.resume(result)
            }
        }
    }

/**
 * Keys written by `firebase-objc-sdk`'s `GenerativeModelError` bridge (>= v0.4.0).
 * See FirebaseAI/Sources/GenerativeModelError.swift in that repo.
 */
private const val KEY_ERROR_TYPE = "KFBErrorType"
private const val KEY_HTTP_STATUS = "KFBHTTPStatusCode"
private const val KEY_HTTP_BODY = "KFBHTTPResponseBody"
private const val KEY_FINISH_REASON = "KFBFinishReason"

/**
 * Reads `userInfo` without an unchecked generic cast. `NSDictionary` bridges
 * to `Map<Any?, *>` in Kotlin/Native and its keys are `NSString`, which
 * compare equal to Kotlin `String` via bridging.
 */
@OptIn(ExperimentalForeignApi::class)
private fun NSError.info(): Map<Any?, *> = userInfo

@OptIn(ExperimentalForeignApi::class)
internal fun NSError.toFirebaseAIException(): FirebaseAIException {
    val info = info()

    val errorType = info[KEY_ERROR_TYPE] as? String
    // NSNumber bridges to Kotlin Number; guard against non-numeric values.
    val httpStatus = (info[KEY_HTTP_STATUS] as? NSNumber)?.intValue
        ?.takeIf { it in 100..599 }
    val responseBody = info[KEY_HTTP_BODY] as? String
    val finishReason = info[KEY_FINISH_REASON] as? String

    val underlying = info[NSUnderlyingErrorKey] as? NSError

    // Preserve the raw localizedDescription. Consumers can format with
    // structured fields (httpStatusCode, underlyingDomain, ...) at the log site.
    val msg = localizedDescription

    return when {
        // PromptBlocked/ResponseStopped on iOS do not currently expose the
        // underlying GenerateContentResponse via NSError.userInfo, so `response`
        // stays null. Revisit if the ObjC bridge starts surfacing it.
        errorType == FirebaseAIErrorType.PROMPT_BLOCKED -> PromptBlockedException(message = msg)
        errorType == FirebaseAIErrorType.RESPONSE_STOPPED_EARLY -> ResponseStoppedException(
            message = msg,
            finishReason = finishReason,
        )
        httpStatus != null && httpStatus in 500..599 -> ServerException(
            message = msg,
            httpStatusCode = httpStatus,
            responseBody = responseBody,
        )
        else -> GenerativeAIException(
            message = msg,
            httpStatusCode = httpStatus,
            responseBody = responseBody,
            errorType = errorType,
            underlyingDomain = underlying?.domain ?: domain,
            underlyingCode = underlying?.code?.toInt() ?: code.toInt(),
        )
    }
}
