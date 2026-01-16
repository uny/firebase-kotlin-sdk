package dev.ynagai.firebase.ai

enum class HarmCategory {
    UNKNOWN,
    HARASSMENT,
    HATE_SPEECH,
    SEXUALLY_EXPLICIT,
    DANGEROUS_CONTENT,
    CIVIC_INTEGRITY,
}

enum class HarmBlockThreshold {
    UNSPECIFIED,
    BLOCK_LOW_AND_ABOVE,
    BLOCK_MEDIUM_AND_ABOVE,
    BLOCK_ONLY_HIGH,
    BLOCK_NONE,
}

enum class HarmProbability {
    UNKNOWN,
    NEGLIGIBLE,
    LOW,
    MEDIUM,
    HIGH,
}

enum class FinishReason {
    UNKNOWN,
    STOP,
    MAX_TOKENS,
    SAFETY,
    RECITATION,
    OTHER,
    BLOCKLIST,
    PROHIBITED_CONTENT,
    SPII,
    MALFORMED_FUNCTION_CALL,
    IMAGE_SAFETY,
}

enum class BlockReason {
    UNKNOWN,
    SAFETY,
    OTHER,
    BLOCKLIST,
    PROHIBITED_CONTENT,
}
