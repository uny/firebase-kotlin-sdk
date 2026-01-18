package dev.ynagai.firebase.ai

import kotlinx.coroutines.flow.Flow

/**
 * A generative AI model that can generate content, count tokens, and create chat sessions.
 *
 * Obtain an instance using [FirebaseAI.generativeModel].
 *
 * @sample
 * ```kotlin
 * val model = Firebase.ai().generativeModel("gemini-2.0-flash")
 *
 * // Generate content
 * val response = model.generateContent("Hello, world!")
 * println(response.text)
 *
 * // Stream content
 * model.generateContentStream("Tell me a story").collect { chunk ->
 *     print(chunk.text)
 * }
 * ```
 */
expect class GenerativeModel {
    /**
     * Generates content based on the provided content parts.
     *
     * @param content The content parts to send to the model.
     * @return The generated content response.
     */
    suspend fun generateContent(vararg content: Content): GenerateContentResponse

    /**
     * Generates content based on a simple text prompt.
     *
     * @param prompt The text prompt to send to the model.
     * @return The generated content response.
     */
    suspend fun generateContent(prompt: String): GenerateContentResponse

    /**
     * Generates content as a stream based on the provided content parts.
     *
     * Use this for real-time streaming of generated content.
     *
     * @param content The content parts to send to the model.
     * @return A [Flow] of [GenerateContentResponse] chunks.
     */
    fun generateContentStream(vararg content: Content): Flow<GenerateContentResponse>

    /**
     * Generates content as a stream based on a simple text prompt.
     *
     * Use this for real-time streaming of generated content.
     *
     * @param prompt The text prompt to send to the model.
     * @return A [Flow] of [GenerateContentResponse] chunks.
     */
    fun generateContentStream(prompt: String): Flow<GenerateContentResponse>

    /**
     * Counts the number of tokens in the provided content.
     *
     * Useful for estimating costs and staying within token limits.
     *
     * @param content The content parts to count tokens for.
     * @return The token count response.
     */
    suspend fun countTokens(vararg content: Content): CountTokensResponse

    /**
     * Counts the number of tokens in a text prompt.
     *
     * Useful for estimating costs and staying within token limits.
     *
     * @param prompt The text prompt to count tokens for.
     * @return The token count response.
     */
    suspend fun countTokens(prompt: String): CountTokensResponse

    /**
     * Starts a new chat session with optional conversation history.
     *
     * The chat session maintains conversation context across multiple messages.
     *
     * @param history Optional list of previous conversation content to initialize the chat.
     * @return A new [Chat] session.
     *
     * @sample
     * ```kotlin
     * val chat = model.startChat()
     * val response1 = chat.sendMessage("Hello!")
     * val response2 = chat.sendMessage("What did I just say?")
     * ```
     */
    fun startChat(history: List<Content> = emptyList()): Chat
}
