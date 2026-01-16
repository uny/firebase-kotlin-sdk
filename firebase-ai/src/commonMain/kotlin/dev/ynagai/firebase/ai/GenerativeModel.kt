package dev.ynagai.firebase.ai

import kotlinx.coroutines.flow.Flow

expect class GenerativeModel {
    suspend fun generateContent(vararg content: Content): GenerateContentResponse
    suspend fun generateContent(prompt: String): GenerateContentResponse
    fun generateContentStream(vararg content: Content): Flow<GenerateContentResponse>
    fun generateContentStream(prompt: String): Flow<GenerateContentResponse>
    suspend fun countTokens(vararg content: Content): CountTokensResponse
    suspend fun countTokens(prompt: String): CountTokensResponse
    fun startChat(history: List<Content> = emptyList()): Chat
}
