package dev.ynagai.firebase.ai

/**
 * Enables context window compression to manage the model's context window.
 *
 * @property triggerTokens The number of tokens that triggers the compression.
 * @property slidingWindow The sliding window compression mechanism.
 */
data class ContextWindowCompressionConfig(
    val triggerTokens: Int? = null,
    val slidingWindow: SlidingWindow? = null,
)

/**
 * Configures the sliding window context compression mechanism.
 *
 * @property targetTokens The session reduction target — how many tokens to keep.
 */
data class SlidingWindow(
    val targetTokens: Int? = null,
)
