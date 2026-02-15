package dev.ynagai.firebase.ai

/**
 * Image format options for Imagen image generation.
 *
 * Use the companion factory methods [png] and [jpeg] to create instances.
 *
 * @sample
 * ```kotlin
 * val config = imagenGenerationConfig {
 *     imageFormat = ImagenImageFormat.jpeg(compressionQuality = 80)
 * }
 * ```
 */
sealed class ImagenImageFormat {
    /** PNG image format. */
    data object Png : ImagenImageFormat()

    /**
     * JPEG image format with optional compression quality.
     *
     * @property compressionQuality Compression quality (0-100). Higher values produce better quality
     *                              but larger file sizes. If null, the server default is used.
     */
    data class Jpeg(val compressionQuality: Int? = null) : ImagenImageFormat()

    companion object {
        /** Creates a PNG image format. */
        fun png(): ImagenImageFormat = Png

        /**
         * Creates a JPEG image format with optional compression quality.
         *
         * @param compressionQuality Compression quality (0-100). If null, the server default is used.
         */
        fun jpeg(compressionQuality: Int? = null): ImagenImageFormat = Jpeg(
            compressionQuality?.also {
                require(it in 0..100) { "compressionQuality must be between 0 and 100, was $it" }
            },
        )
    }
}
