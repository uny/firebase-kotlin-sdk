package dev.ynagai.firebase.ai

/**
 * Response from an Imagen image generation request.
 *
 * @property images The list of generated images.
 * @property filteredReason The reason images were filtered, if any.
 */
data class ImagenGenerationResponse(
    val images: List<ImagenInlineImage> = emptyList(),
    val filteredReason: String? = null,
)

/**
 * An inline image returned from Imagen image generation.
 *
 * @property data The raw image data as a byte array.
 * @property mimeType The MIME type of the image (e.g., "image/png", "image/jpeg").
 */
data class ImagenInlineImage(
    val data: ByteArray,
    val mimeType: String,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false
        other as ImagenInlineImage
        if (!data.contentEquals(other.data)) return false
        if (mimeType != other.mimeType) return false
        return true
    }

    override fun hashCode(): Int {
        var result = data.contentHashCode()
        result = 31 * result + mimeType.hashCode()
        return result
    }
}
