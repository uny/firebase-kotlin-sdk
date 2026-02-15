package dev.ynagai.firebase.ai

import dev.ynagai.firebase.Firebase
import dev.ynagai.firebase.FirebaseApp
import dev.ynagai.firebase.app

/**
 * Returns the [FirebaseAI] instance for the given [app] and [backend].
 *
 * Use this function to access Firebase AI services for generative AI capabilities.
 *
 * @param app The [FirebaseApp] instance to use. Defaults to the default Firebase app.
 * @param backend The [GenerativeBackend] to use (Google AI or Vertex AI). Defaults to Google AI.
 * @param useLimitedUseAppCheckTokens Whether to use limited-use App Check tokens for enhanced security.
 * @return The [FirebaseAI] instance.
 *
 * @sample
 * ```kotlin
 * val ai = Firebase.ai()
 * val model = ai.generativeModel("gemini-2.0-flash")
 * ```
 */
expect fun Firebase.ai(
    app: FirebaseApp = Firebase.app,
    backend: GenerativeBackend = GenerativeBackend.googleAI(),
    useLimitedUseAppCheckTokens: Boolean = false,
): FirebaseAI

/**
 * Entry point for Firebase AI services.
 *
 * Use [Firebase.ai] to obtain an instance of this class, then use [generativeModel]
 * to create a [GenerativeModel] for content generation.
 *
 * @sample
 * ```kotlin
 * val ai = Firebase.ai()
 * val model = ai.generativeModel(
 *     modelName = "gemini-2.0-flash",
 *     generationConfig = generationConfig {
 *         temperature = 0.7f
 *         maxOutputTokens = 1024
 *     }
 * )
 * ```
 */
expect class FirebaseAI {
    /**
     * Creates a [GenerativeModel] with the specified configuration.
     *
     * @param modelName The name of the generative model to use (e.g., "gemini-2.0-flash").
     * @param generationConfig Optional configuration for content generation (temperature, max tokens, etc.).
     * @param safetySettings Optional list of safety settings to control content filtering.
     * @param systemInstruction Optional system instruction to guide the model's behavior.
     * @return A configured [GenerativeModel] instance.
     */
    fun generativeModel(
        modelName: String,
        generationConfig: GenerationConfig? = null,
        safetySettings: List<SafetySetting>? = null,
        systemInstruction: Content? = null,
        tools: List<Tool>? = null,
        toolConfig: ToolConfig? = null,
    ): GenerativeModel

    /**
     * Creates an [ImagenModel] with the specified configuration.
     *
     * @param modelName The name of the Imagen model to use (e.g., "imagen-3.0-generate-002").
     * @param generationConfig Optional configuration for image generation.
     * @param safetySettings Optional safety settings for image generation.
     * @return A configured [ImagenModel] instance.
     *
     * @sample
     * ```kotlin
     * val model = Firebase.ai().imagenModel(
     *     modelName = "imagen-3.0-generate-002",
     *     generationConfig = imagenGenerationConfig {
     *         numberOfImages = 4
     *         aspectRatio = ImagenAspectRatio.LANDSCAPE_16x9
     *     }
     * )
     * ```
     */
    fun imagenModel(
        modelName: String,
        generationConfig: ImagenGenerationConfig? = null,
        safetySettings: ImagenSafetySettings? = null,
    ): ImagenModel
}
