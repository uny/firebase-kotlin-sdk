package dev.ynagai.firebase.ai

/**
 * A generative AI model configured for live (real-time) streaming sessions.
 *
 * Obtain an instance using [FirebaseAI.liveModel].
 *
 * @sample
 * ```kotlin
 * val model = Firebase.ai().liveModel(
 *     modelName = "gemini-2.0-flash-live-001",
 *     liveGenerationConfig = liveGenerationConfig {
 *         responseModality = ResponseModality.TEXT
 *     }
 * )
 * val session = model.connect()
 * ```
 */
expect class LiveGenerativeModel {
    /**
     * Connects to the server and establishes a live session.
     *
     * @return A [LiveSession] for sending and receiving real-time content.
     */
    suspend fun connect(): LiveSession
}
