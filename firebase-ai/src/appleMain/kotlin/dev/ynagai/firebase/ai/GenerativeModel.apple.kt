package dev.ynagai.firebase.ai

import kotlinx.coroutines.flow.Flow

actual class GenerativeModel {
    actual suspend fun generateContent(vararg content: Content): GenerateContentResponse {
        TODO("iOS implementation pending")
    }

    actual suspend fun generateContent(prompt: String): GenerateContentResponse {
        TODO("iOS implementation pending")
    }

    actual fun generateContentStream(vararg content: Content): Flow<GenerateContentResponse> {
        TODO("iOS implementation pending")
    }

    actual fun generateContentStream(prompt: String): Flow<GenerateContentResponse> {
        TODO("iOS implementation pending")
    }

    actual suspend fun countTokens(vararg content: Content): CountTokensResponse {
        TODO("iOS implementation pending")
    }

    actual suspend fun countTokens(prompt: String): CountTokensResponse {
        TODO("iOS implementation pending")
    }

    actual fun startChat(history: List<Content>): Chat {
        TODO("iOS implementation pending")
    }
}

