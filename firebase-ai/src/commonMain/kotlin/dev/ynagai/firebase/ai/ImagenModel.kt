package dev.ynagai.firebase.ai

/**
 * An Imagen model that can generate images from text prompts.
 *
 * Obtain an instance using [FirebaseAI.imagenModel].
 *
 * On Apple platforms [generateImages] always throws [FirebaseAIException]:
 * FirebaseAILogic 12.18.0 removed the Imagen API.
 *
 * @sample
 * ```kotlin
 * val model = Firebase.ai().imagenModel("imagen-3.0-generate-002")
 * val response = model.generateImages("A cat sitting on a windowsill")
 * val imageData = response.images.first().data
 * ```
 */
@Suppress("DEPRECATION")
@Deprecated(IMAGEN_DEPRECATION_MESSAGE)
expect class ImagenModel {
    /**
     * Generates images from a text prompt.
     *
     * @param prompt The text prompt describing the images to generate.
     * @return The image generation response containing generated images.
     * @throws FirebaseAIException if the image generation fails.
     */
    suspend fun generateImages(prompt: String): ImagenGenerationResponse
}
