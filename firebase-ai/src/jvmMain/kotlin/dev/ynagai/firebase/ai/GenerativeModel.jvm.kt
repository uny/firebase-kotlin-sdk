package dev.ynagai.firebase.ai

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@Suppress("UNUSED_PARAMETER")
actual class GenerativeModel internal constructor() {
    actual suspend fun generateContent(vararg content: Content): GenerateContentResponse =
        unavailableOnJvm()

    actual suspend fun generateContent(prompt: String): GenerateContentResponse =
        unavailableOnJvm()

    actual fun generateContentStream(vararg content: Content): Flow<GenerateContentResponse> =
        flow { unavailableOnJvm() }

    actual fun generateContentStream(prompt: String): Flow<GenerateContentResponse> =
        flow { unavailableOnJvm() }

    actual suspend fun countTokens(vararg content: Content): CountTokensResponse =
        unavailableOnJvm()

    actual suspend fun countTokens(prompt: String): CountTokensResponse =
        unavailableOnJvm()

    actual fun startChat(history: List<Content>): Chat = Chat(history)
}
