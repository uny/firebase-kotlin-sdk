package dev.ynagai.firebase.ai

import kotlinx.coroutines.flow.Flow

/**
 * A live session for real-time bidirectional streaming with a generative AI model.
 *
 * Use [receive] to get a [Flow] of server messages, and the various `send*` methods
 * to send content to the model.
 *
 * @sample
 * ```kotlin
 * val session = model.connect()
 *
 * // Receive messages
 * session.receive().collect { message ->
 *     when (message) {
 *         is LiveServerMessage.Content -> println(message.content?.parts)
 *         is LiveServerMessage.ToolCall -> println(message.functionCalls)
 *         is LiveServerMessage.ToolCallCancellation -> println(message.ids)
 *         is LiveServerMessage.GoingAway -> println("Server disconnecting")
 *     }
 * }
 *
 * // Send content
 * session.send(Content(role = "user", parts = listOf(TextPart("Hello!"))))
 * session.close()
 * ```
 */
expect class LiveSession {
    /**
     * Returns a [Flow] of [LiveServerMessage]s received from the server.
     */
    fun receive(): Flow<LiveServerMessage>

    /**
     * Sends content to the model.
     *
     * @param content The content to send.
     * @param turnComplete Whether this completes the user's turn. Defaults to true.
     */
    suspend fun send(content: Content, turnComplete: Boolean = true)

    /**
     * Sends text in real-time to the model.
     *
     * @param text The text to send.
     */
    suspend fun sendTextRealtime(text: String)

    /**
     * Sends media data in real-time to the model.
     *
     * @param data The inline data part containing the media to send.
     */
    suspend fun sendMediaRealtime(data: InlineDataPart)

    /**
     * Sends function responses back to the model.
     *
     * @param functionList The list of function response parts to send.
     */
    suspend fun sendFunctionResponses(functionList: List<FunctionResponsePart>)

    /**
     * Closes the live session.
     */
    fun close()
}
