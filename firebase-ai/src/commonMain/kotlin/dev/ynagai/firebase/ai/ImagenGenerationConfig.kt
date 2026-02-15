package dev.ynagai.firebase.ai

/**
 * Configuration options for Imagen image generation.
 *
 * Use [imagenGenerationConfig] DSL function to create instances.
 *
 * @property negativePrompt A text prompt describing what to exclude from generated images.
 * @property numberOfImages Number of images to generate. Defaults to server-side default if null.
 * @property aspectRatio The aspect ratio of generated images.
 * @property imageFormat The image format for generated images (PNG or JPEG).
 * @property addWatermark Whether to add a watermark to generated images. Null means server default.
 *
 * @sample
 * ```kotlin
 * val config = imagenGenerationConfig {
 *     negativePrompt = "blurry, low quality"
 *     numberOfImages = 4
 *     aspectRatio = ImagenAspectRatio.LANDSCAPE_16x9
 *     imageFormat = ImagenImageFormat.jpeg(compressionQuality = 80)
 *     addWatermark = true
 * }
 * ```
 */
data class ImagenGenerationConfig(
    val negativePrompt: String? = null,
    val numberOfImages: Int? = null,
    val aspectRatio: ImagenAspectRatio? = null,
    val imageFormat: ImagenImageFormat? = null,
    val addWatermark: Boolean? = null,
)

/**
 * DSL marker for the Imagen generation config builder.
 */
@DslMarker
annotation class ImagenGenerationConfigDsl

/**
 * Builder class for constructing [ImagenGenerationConfig] instances using a DSL.
 *
 * @see imagenGenerationConfig
 */
@ImagenGenerationConfigDsl
class ImagenGenerationConfigBuilder {
    /** A text prompt describing what to exclude from generated images. */
    var negativePrompt: String? = null
    /** Number of images to generate. */
    var numberOfImages: Int? = null
    /** The aspect ratio of generated images. */
    var aspectRatio: ImagenAspectRatio? = null
    /** The image format for generated images (PNG or JPEG). */
    var imageFormat: ImagenImageFormat? = null
    /** Whether to add a watermark to generated images. */
    var addWatermark: Boolean? = null

    internal fun build(): ImagenGenerationConfig = ImagenGenerationConfig(
        negativePrompt = negativePrompt,
        numberOfImages = numberOfImages,
        aspectRatio = aspectRatio,
        imageFormat = imageFormat,
        addWatermark = addWatermark,
    )
}

/**
 * Creates an [ImagenGenerationConfig] using a DSL builder.
 *
 * @param block The builder block for configuring image generation parameters.
 * @return A new [ImagenGenerationConfig] instance.
 *
 * @sample
 * ```kotlin
 * val config = imagenGenerationConfig {
 *     negativePrompt = "blurry"
 *     numberOfImages = 2
 *     aspectRatio = ImagenAspectRatio.SQUARE_1x1
 * }
 * ```
 */
fun imagenGenerationConfig(block: ImagenGenerationConfigBuilder.() -> Unit): ImagenGenerationConfig {
    val builder = ImagenGenerationConfigBuilder()
    builder.block()
    return builder.build()
}
