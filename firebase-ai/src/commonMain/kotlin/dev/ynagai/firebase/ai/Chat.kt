package dev.ynagai.firebase.ai

import kotlinx.coroutines.flow.Flow

/**
 * A multi-turn chat session with a generative AI model.
 *
 * The chat maintains conversation history, allowing the model to reference
 * previous messages for context-aware responses.
 *
 * Obtain an instance using [GenerativeModel.startChat].
 *
 * @sample
 * ```kotlin
 * val chat = model.startChat()
 *
 * val response1 = chat.sendMessage("My name is Alice")
 * println(response1.text)
 *
 * val response2 = chat.sendMessage("What's my name?")
 * println(response2.text) // Should reference "Alice"
 *
 * // Access full conversation history
 * chat.history.forEach { content ->
 *     println("${content.role}: ${content.parts}")
 * }
 * ```
 */
expect class Chat {
    /**
     * The conversation history, including all user and model messages.
     *
     * This list grows as messages are exchanged and is used by the model
     * to maintain context across the conversation.
     */
    val history: List<Content>

    /**
     * Sends a message to the model and returns the response.
     *
     * The message and response are automatically added to [history].
     *
     * @param content The content to send to the model.
     * @return The generated content response.
     */
    suspend fun sendMessage(content: Content): GenerateContentResponse

    /**
     * Sends a text message to the model and returns the response.
     *
     * The message and response are automatically added to [history].
     *
     * @param prompt The text message to send to the model.
     * @return The generated content response.
     */
    suspend fun sendMessage(prompt: String): GenerateContentResponse

    /**
     * Sends a message to the model and streams the response.
     *
     * The message and complete response are added to [history] after streaming completes.
     *
     * @param content The content to send to the model.
     * @return A [Flow] of [GenerateContentResponse] chunks.
     */
    fun sendMessageStream(content: Content): Flow<GenerateContentResponse>

    /**
     * Sends a text message to the model and streams the response.
     *
     * The message and complete response are added to [history] after streaming completes.
     *
     * @param prompt The text message to send to the model.
     * @return A [Flow] of [GenerateContentResponse] chunks.
     */
    fun sendMessageStream(prompt: String): Flow<GenerateContentResponse>
}
