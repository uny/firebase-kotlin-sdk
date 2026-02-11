package dev.ynagai.firebase.ai

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import swiftPMImport.dev.ynagai.firebase.firebase.ai.KFBChat
import swiftPMImport.dev.ynagai.firebase.firebase.ai.KFBGenerateContentResponse
import swiftPMImport.dev.ynagai.firebase.firebase.ai.KFBModelContent

@OptIn(ExperimentalForeignApi::class)
actual class Chat internal constructor(
    internal val apple: KFBChat,
) {
    @Suppress("UNCHECKED_CAST")
    actual val history: List<Content>
        get() = (apple.history() as? List<KFBModelContent>)?.map { it.toCommon() } ?: emptyList()

    actual suspend fun sendMessage(content: Content): GenerateContentResponse {
        val result = awaitResult<KFBGenerateContentResponse> { callback ->
            apple.sendMessage(listOf(content.toApple()), completionHandler = callback)
        }
        return result.toCommon()
    }

    actual suspend fun sendMessage(prompt: String): GenerateContentResponse {
        val result = awaitResult<KFBGenerateContentResponse> { callback ->
            apple.sendMessageText(prompt, completionHandler = callback)
        }
        return result.toCommon()
    }

    actual fun sendMessageStream(content: Content): Flow<GenerateContentResponse> =
        flow { emit(sendMessage(content)) }

    actual fun sendMessageStream(prompt: String): Flow<GenerateContentResponse> =
        flow { emit(sendMessage(prompt)) }
}
