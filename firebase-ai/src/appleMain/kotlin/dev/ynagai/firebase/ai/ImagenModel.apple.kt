@file:Suppress("DEPRECATION")

package dev.ynagai.firebase.ai

/**
 * FirebaseAILogic 12.18.0 removed the Imagen API, so there is no native model to call on
 * Apple platforms. [generateImages] always throws.
 */
actual class ImagenModel internal constructor() {
    actual suspend fun generateImages(prompt: String): ImagenGenerationResponse =
        throw FirebaseAIException(
            "Imagen is not available on Apple platforms: FirebaseAILogic removed the Imagen API " +
                "after the Imagen models were shut down. Use Gemini image generation instead.",
        )
}
