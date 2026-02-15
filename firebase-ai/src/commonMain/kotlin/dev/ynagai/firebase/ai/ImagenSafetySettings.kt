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
data class ImagenSafetySettings(
    val safetyFilterLevel: ImagenSafetyFilterLevel? = null,
    val personFilterLevel: ImagenPersonFilterLevel? = null,
)
