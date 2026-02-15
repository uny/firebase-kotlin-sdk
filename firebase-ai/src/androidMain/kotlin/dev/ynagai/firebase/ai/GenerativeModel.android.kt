package dev.ynagai.firebase.ai

import com.google.firebase.ai.GenerativeModel as AndroidGenerativeModel
import com.google.firebase.ai.type.CountTokensResponse as AndroidCountTokensResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
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
        return android.generateContentStream(androidContents)
            .map { it.toCommon() }
            .catch { throw mapAndroidException(it) }
    }

    actual fun generateContentStream(prompt: String): Flow<GenerateContentResponse> =
        android.generateContentStream(prompt)
            .map { it.toCommon() }
            .catch { throw mapAndroidException(it) }

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
