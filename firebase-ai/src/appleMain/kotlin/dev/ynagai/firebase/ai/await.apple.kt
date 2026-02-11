package dev.ynagai.firebase.ai

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
internal fun NSError.toFirebaseAIException(): FirebaseAIException =
    FirebaseAIException(
        message = localizedDescription,
        cause = Exception(localizedDescription),
    )
