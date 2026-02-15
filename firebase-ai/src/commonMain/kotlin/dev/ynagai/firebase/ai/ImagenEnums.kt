package dev.ynagai.firebase.ai

/**
 * Aspect ratios for Imagen image generation.
 */
enum class ImagenAspectRatio {
    /** 1:1 square aspect ratio. */
    SQUARE_1x1,
    /** 9:16 portrait aspect ratio. */
    PORTRAIT_9x16,
    /** 16:9 landscape aspect ratio. */
    LANDSCAPE_16x9,
    /** 3:4 portrait aspect ratio. */
    PORTRAIT_3x4,
    /** 4:3 landscape aspect ratio. */
    LANDSCAPE_4x3,
}

/**
 * Safety filter levels for Imagen image generation.
 *
 * Controls how aggressively the model filters potentially unsafe content.
 */
enum class ImagenSafetyFilterLevel {
    /** Block content with low probability of being unsafe and above. */
    BLOCK_LOW_AND_ABOVE,
    /** Block content with medium probability of being unsafe and above. */
    BLOCK_MEDIUM_AND_ABOVE,
    /** Only block content with high probability of being unsafe. */
    BLOCK_ONLY_HIGH,
    /** Do not block any content. */
    BLOCK_NONE,
}

/**
 * Person filter levels for Imagen image generation.
 *
 * Controls whether generated images can contain people.
 */
enum class ImagenPersonFilterLevel {
    /** Block all images containing people. */
    BLOCK_ALL,
    /** Allow images containing adults only. */
    ALLOW_ADULT,
    /** Allow all images containing people. */
    ALLOW_ALL,
}
