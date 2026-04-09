package dev.ynagai.firebase.ai

/**
 * Safety settings for Imagen image generation.
 *
 * Controls content filtering for generated images.
 *
 * @property safetyFilterLevel The level of safety filtering to apply.
 * @property personFilterLevel Controls whether generated images can contain people.
 *
 * @sample
 * ```kotlin
 * val safety = ImagenSafetySettings(
 *     safetyFilterLevel = ImagenSafetyFilterLevel.BLOCK_MEDIUM_AND_ABOVE,
 *     personFilterLevel = ImagenPersonFilterLevel.ALLOW_ADULT,
 * )
 * ```
 */
@Deprecated(
    "Imagen models are deprecated and will be shut down. " +
        "Migrate to Gemini image generation models. " +
        "See https://firebase.google.com/docs/ai-logic/generate-images-gemini",
)
data class ImagenSafetySettings(
    val safetyFilterLevel: ImagenSafetyFilterLevel,
    val personFilterLevel: ImagenPersonFilterLevel,
)
