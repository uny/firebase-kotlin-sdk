package dev.ynagai.firebase.ai

/**
 * Content modality types for token counting.
 */
enum class ContentModality {
    TEXT,
    IMAGE,
    AUDIO,
    VIDEO,
    DOCUMENT,
    UNSPECIFIED,
}

/**
 * Token count for a specific content modality.
 *
 * @property modality The content modality (e.g., text, image, audio).
 * @property tokenCount The number of tokens for this modality.
 */
data class ModalityTokenCount(
    val modality: ContentModality,
    val tokenCount: Int,
)
