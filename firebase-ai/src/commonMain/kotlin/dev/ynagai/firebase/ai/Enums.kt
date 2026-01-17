package dev.ynagai.firebase.ai

/**
 * Categories of potential harm in content.
 *
 * Used with [SafetySetting] to configure content filtering.
 */
enum class HarmCategory {
    /** Unknown or unspecified harm category. */
    UNKNOWN,
    /** Content that harasses or bullies individuals or groups. */
    HARASSMENT,
    /** Content that promotes hate against individuals or groups. */
    HATE_SPEECH,
    /** Sexually explicit content. */
    SEXUALLY_EXPLICIT,
    /** Content that promotes dangerous activities. */
    DANGEROUS_CONTENT,
    /** Content that may undermine civic integrity. */
    CIVIC_INTEGRITY,
}

/**
 * Thresholds for blocking harmful content.
 *
 * Used with [SafetySetting] to control sensitivity of content filtering.
 */
enum class HarmBlockThreshold {
    /** Threshold is unspecified. */
    UNSPECIFIED,
    /** Block content with low probability of harm and above. */
    BLOCK_LOW_AND_ABOVE,
    /** Block content with medium probability of harm and above. */
    BLOCK_MEDIUM_AND_ABOVE,
    /** Only block content with high probability of harm. */
    BLOCK_ONLY_HIGH,
    /** Do not block any content (use with caution). */
    BLOCK_NONE,
}

/**
 * Probability levels for harmful content detection.
 *
 * Indicates how likely content is to be harmful.
 */
enum class HarmProbability {
    /** Unknown probability. */
    UNKNOWN,
    /** Negligible probability of harm. */
    NEGLIGIBLE,
    /** Low probability of harm. */
    LOW,
    /** Medium probability of harm. */
    MEDIUM,
    /** High probability of harm. */
    HIGH,
}

/**
 * Reasons why content generation finished.
 */
enum class FinishReason {
    /** Unknown or unspecified reason. */
    UNKNOWN,
    /** Natural stop point reached. */
    STOP,
    /** Maximum token limit reached. */
    MAX_TOKENS,
    /** Stopped due to safety concerns. */
    SAFETY,
    /** Stopped due to recitation/copyright concerns. */
    RECITATION,
    /** Stopped for other reasons. */
    OTHER,
    /** Content matched a blocklist. */
    BLOCKLIST,
    /** Content was prohibited. */
    PROHIBITED_CONTENT,
    /** Sensitive personally identifiable information detected. */
    SPII,
    /** Function call was malformed. */
    MALFORMED_FUNCTION_CALL,
    /** Image content failed safety checks. */
    IMAGE_SAFETY,
}

/**
 * Reasons why a prompt was blocked.
 */
enum class BlockReason {
    /** Unknown or unspecified reason. */
    UNKNOWN,
    /** Blocked due to safety concerns. */
    SAFETY,
    /** Blocked for other reasons. */
    OTHER,
    /** Prompt matched a blocklist. */
    BLOCKLIST,
    /** Prompt contained prohibited content. */
    PROHIBITED_CONTENT,
}
