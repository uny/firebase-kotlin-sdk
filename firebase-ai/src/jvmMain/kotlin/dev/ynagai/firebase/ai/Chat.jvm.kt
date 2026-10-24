package dev.ynagai.firebase.ai

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@Suppress("UNUSED_PARAMETER")
actual class Chat internal constructor(
    actual val history: List<Content>,
) {
    actual suspend fun sendMessage(content: Content): GenerateContentResponse =
        unavailableOnJvm()

    actual suspend fun sendMessage(prompt: String): GenerateContentResponse =
        unavailableOnJvm()

    actual fun sendMessageStream(content: Content): Flow<GenerateContentResponse> =
        flow { unavailableOnJvm() }

    actual fun sendMessageStream(prompt: String): Flow<GenerateContentResponse> =
        flow { unavailableOnJvm() }
}
