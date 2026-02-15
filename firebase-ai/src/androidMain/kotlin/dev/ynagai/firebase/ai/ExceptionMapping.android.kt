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
    is AndroidResponseStoppedException ->
        ResponseStoppedException(message = e.message, response = e.response?.toCommon())
    is AndroidInvalidAPIKeyException ->
        InvalidAPIKeyException(message = e.message, cause = e)
    is AndroidQuotaExceededException ->
        QuotaExceededException(message = e.message, cause = e)
    is AndroidServerException ->
        ServerException(message = e.message, cause = e)
    is com.google.firebase.ai.type.FirebaseAIException ->
        FirebaseAIException(message = e.message, cause = e)
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
