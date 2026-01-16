package dev.ynagai.firebase.ai

import kotlinx.coroutines.flow.Flow

expect class Chat {
    val history: List<Content>
    suspend fun sendMessage(content: Content): GenerateContentResponse
    suspend fun sendMessage(prompt: String): GenerateContentResponse
    fun sendMessageStream(content: Content): Flow<GenerateContentResponse>
    fun sendMessageStream(prompt: String): Flow<GenerateContentResponse>
}
