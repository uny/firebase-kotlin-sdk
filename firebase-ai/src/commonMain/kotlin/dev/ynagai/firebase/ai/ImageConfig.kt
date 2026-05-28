package dev.ynagai.firebase.ai

data class ImageConfig(
    val aspectRatio: AspectRatio? = null,
    val imageSize: ImageSize? = null,
)

enum class AspectRatio {
    SQUARE_1x1,
    PORTRAIT_2x3,
    LANDSCAPE_3x2,
    PORTRAIT_3x4,
    LANDSCAPE_4x3,
    PORTRAIT_4x5,
    LANDSCAPE_5x4,
    PORTRAIT_9x16,
    LANDSCAPE_16x9,
    LANDSCAPE_21x9,
}

enum class ImageSize {
    SIZE_512,
    SIZE_1K,
    SIZE_2K,
    SIZE_4K,
}
