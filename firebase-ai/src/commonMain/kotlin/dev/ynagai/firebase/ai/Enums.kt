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
    /** Image content classified as hateful. */
    IMAGE_HATE,
    /** Image content classified as dangerous. */
    IMAGE_DANGEROUS_CONTENT,
    /** Image content classified as harassment. */
    IMAGE_HARASSMENT,
    /** Image content classified as sexually explicit. */
    IMAGE_SEXUALLY_EXPLICIT,
}

/**
 * Thresholds for blocking harmful content.
 *
 * Used with [SafetySetting] to control sensitivity of content filtering.
 */
enum class HarmBlockThreshold {
    /** Block content with low probability of harm and above. */
    LOW_AND_ABOVE,
    /** Block content with medium probability of harm and above. */
    MEDIUM_AND_ABOVE,
    /** Only block content with high probability of harm. */
    ONLY_HIGH,
    /** Do not block any content (use with caution). */
    NONE,
    /** Turn off the safety filter entirely. Same as NONE but without metadata. */
    OFF,
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
