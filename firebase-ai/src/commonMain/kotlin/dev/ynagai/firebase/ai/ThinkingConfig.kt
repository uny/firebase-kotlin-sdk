package dev.ynagai.firebase.ai

/**
 * Predefined thinking budget levels for the model.
 */
enum class ThinkingLevel {
    MINIMAL,
    LOW,
    MEDIUM,
    HIGH,
}

/**
 * Configuration for the model's thinking process.
 *
 * Use either [thinkingBudget] for an explicit token count, or [thinkingLevel] for a predefined level.
 *
 * @property thinkingBudget The maximum number of thinking tokens, or `null` for the default.
 * @property thinkingLevel The thinking budget level, or `null` to use [thinkingBudget] / default.
 * @property includeThoughts Whether thought parts should be included in responses.
 */
data class ThinkingConfig(
    val thinkingBudget: Int? = null,
    val thinkingLevel: ThinkingLevel? = null,
    val includeThoughts: Boolean? = null,
)
