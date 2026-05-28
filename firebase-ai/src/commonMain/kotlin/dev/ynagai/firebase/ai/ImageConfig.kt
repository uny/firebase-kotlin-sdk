package dev.ynagai.firebase.ai

data class ImageConfig(
    val aspectRatio: AspectRatio? = null,
    val imageSize: ImageSize? = null,
)

enum class AspectRatio(internal val value: String) {
    SQUARE_1x1("1:1"),
    PORTRAIT_2x3("2:3"),
    LANDSCAPE_3x2("3:2"),
    PORTRAIT_3x4("3:4"),
    LANDSCAPE_4x3("4:3"),
    PORTRAIT_4x5("4:5"),
    LANDSCAPE_5x4("5:4"),
    PORTRAIT_9x16("9:16"),
    LANDSCAPE_16x9("16:9"),
    LANDSCAPE_21x9("21:9"),
}

enum class ImageSize(internal val value: String) {
    SIZE_512("512"),
    SIZE_1K("1K"),
    SIZE_2K("2K"),
    SIZE_4K("4K"),
}
