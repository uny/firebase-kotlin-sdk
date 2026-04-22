package dev.ynagai.firebase.ai

import com.google.firebase.ai.type.PromptBlockedException as AndroidPromptBlockedException
import com.google.firebase.ai.type.ResponseStoppedException as AndroidResponseStoppedException
import com.google.firebase.ai.type.InvalidAPIKeyException as AndroidInvalidAPIKeyException
import com.google.firebase.ai.type.QuotaExceededException as AndroidQuotaExceededException
import com.google.firebase.ai.type.ServerException as AndroidServerException

/**
 * Maps an Android Firebase AI exception to the corresponding KMP common exception.
 */
internal fun mapAndroidException(e: Throwable): Throwable = when (e) {
    is AndroidPromptBlockedException ->
        PromptBlockedException(message = e.message, response = e.response?.toCommon())
    is AndroidResponseStoppedException -> {
        val resp = e.response
        ResponseStoppedException(
            message = e.message,
            response = resp?.toCommon(),
            finishReason = resp?.candidates?.firstOrNull()?.finishReason?.name,
        )
    }
    is AndroidInvalidAPIKeyException ->
        InvalidAPIKeyException(message = e.message, cause = e)
    is AndroidQuotaExceededException ->
        QuotaExceededException(message = e.message, cause = e)
    is AndroidServerException ->
        ServerException(
            message = e.message,
            cause = e,
            // Google's Android SDK does not expose HTTP status or response body
            // as structured fields; fall back to a best-effort message parser.
            httpStatusCode = parseHttpStatusFromMessage(e.message),
        )
    // Fallback for Android FirebaseAIException subclasses we don't map explicitly.
    // underlyingDomain is only populated here — typed subclasses above preserve the
    // original Android class via the `cause` chain instead.
    is com.google.firebase.ai.type.FirebaseAIException ->
        FirebaseAIException(
            message = e.message,
            cause = e,
            underlyingDomain = e::class.qualifiedName,
        )
    else -> e
}

/**
 * Wraps a suspend block, catching Android Firebase AI exceptions and rethrowing
 * them as KMP common exceptions.
 */
internal inline fun <T> wrapAndroidException(block: () -> T): T = try {
    block()
} catch (e: Throwable) {
    throw mapAndroidException(e)
}
