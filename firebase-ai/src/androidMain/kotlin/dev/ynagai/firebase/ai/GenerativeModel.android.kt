package dev.ynagai.firebase.ai

import com.google.firebase.ai.GenerativeModel as AndroidGenerativeModel
import com.google.firebase.ai.type.CountTokensResponse as AndroidCountTokensResponse
import com.google.firebase.ai.type.PromptBlockedException as AndroidPromptBlockedException
import com.google.firebase.ai.type.ResponseStoppedException as AndroidResponseStoppedException
import com.google.firebase.ai.type.InvalidAPIKeyException as AndroidInvalidAPIKeyException
import com.google.firebase.ai.type.QuotaExceededException as AndroidQuotaExceededException
import com.google.firebase.ai.type.ServerException as AndroidServerException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

actual class GenerativeModel internal constructor(
    internal val android: AndroidGenerativeModel
) {
    actual suspend fun generateContent(vararg content: Content): GenerateContentResponse =
        wrapAndroidException {
            val androidContents = content.map { it.toAndroid() }
            android.generateContent(androidContents).toCommon()
        }

    actual suspend fun generateContent(prompt: String): GenerateContentResponse =
        wrapAndroidException {
            android.generateContent(prompt).toCommon()
        }

    actual fun generateContentStream(vararg content: Content): Flow<GenerateContentResponse> {
        val androidContents = content.map { it.toAndroid() }
        return android.generateContentStream(androidContents).map { it.toCommon() }
    }

    actual fun generateContentStream(prompt: String): Flow<GenerateContentResponse> =
        android.generateContentStream(prompt).map { it.toCommon() }

    actual suspend fun countTokens(vararg content: Content): CountTokensResponse =
        wrapAndroidException {
            val androidContents = content.map { it.toAndroid() }
            android.countTokens(androidContents).toCommon()
        }

    actual suspend fun countTokens(prompt: String): CountTokensResponse =
        wrapAndroidException {
            android.countTokens(prompt).toCommon()
        }

    actual fun startChat(history: List<Content>): Chat =
        Chat(android.startChat(history.map { it.toAndroid() }))
}

internal fun AndroidCountTokensResponse.toCommon(): CountTokensResponse = CountTokensResponse(
    totalTokens = totalTokens,
)

private inline fun <T> wrapAndroidException(block: () -> T): T = try {
    block()
} catch (e: AndroidPromptBlockedException) {
    throw PromptBlockedException(message = e.message, response = e.response?.toCommon())
} catch (e: AndroidResponseStoppedException) {
    throw ResponseStoppedException(message = e.message, response = e.response?.toCommon())
} catch (e: AndroidInvalidAPIKeyException) {
    throw InvalidAPIKeyException(message = e.message, cause = e)
} catch (e: AndroidQuotaExceededException) {
    throw QuotaExceededException(message = e.message, cause = e)
} catch (e: AndroidServerException) {
    throw ServerException(message = e.message, cause = e)
} catch (e: com.google.firebase.ai.type.FirebaseAIException) {
    throw FirebaseAIException(message = e.message, cause = e)
}
