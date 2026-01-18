package dev.ynagai.firebase.ai

import kotlinx.coroutines.flow.Flow

actual class Chat {
    actual val history: List<Content>
        get() = TODO("iOS implementation pending")

    actual suspend fun sendMessage(content: Content): GenerateContentResponse {
        TODO("iOS implementation pending")
    }

    actual suspend fun sendMessage(prompt: String): GenerateContentResponse {
        TODO("iOS implementation pending")
    }

    actual fun sendMessageStream(content: Content): Flow<GenerateContentResponse> {
        TODO("iOS implementation pending")
    }

    actual fun sendMessageStream(prompt: String): Flow<GenerateContentResponse> {
        TODO("iOS implementation pending")
    }
}
