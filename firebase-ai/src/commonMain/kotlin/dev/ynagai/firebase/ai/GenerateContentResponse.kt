package dev.ynagai.firebase.ai

/**
 * Response from a content generation request.
 *
 * Contains one or more [Candidate] responses, along with metadata about
 * the request and response.
 *
 * @property candidates List of generated response candidates.
 * @property promptFeedback Feedback about the prompt, including any blocking reasons.
 * @property usageMetadata Token usage statistics for the request.
 */
data class GenerateContentResponse(
    val candidates: List<Candidate> = emptyList(),
    val promptFeedback: PromptFeedback? = null,
    val usageMetadata: UsageMetadata? = null,
) {
    /**
     * Convenience property to extract the text from the first candidate.
     *
     * Returns `null` if there are no candidates or no text parts.
     */
    val text: String?
        get() = candidates.firstOrNull()?.content?.parts
            ?.filterIsInstance<TextPart>()
            ?.joinToString("") { it.text }
            ?.takeIf { it.isNotEmpty() }
}

/**
 * A single candidate response from the model.
 *
 * @property content The generated content.
 * @property finishReason The reason why generation stopped.
 * @property safetyRatings Safety ratings for the response.
 * @property citationMetadata Citation information for attributed content.
 */
data class Candidate(
    val content: Content = Content(),
    val finishReason: FinishReason? = null,
    val safetyRatings: List<SafetyRating> = emptyList(),
    val citationMetadata: CitationMetadata? = null,
)

/**
 * Safety rating for a piece of content.
 *
 * @property category The harm category being rated.
 * @property probability The probability of harm.
 * @property blocked Whether the content was blocked due to this rating.
 */
data class SafetyRating(
    val category: HarmCategory,
    val probability: HarmProbability,
    val blocked: Boolean = false,
)

/**
 * Metadata about citations in the generated content.
 *
 * @property citations List of citations for attributed content.
 */
data class CitationMetadata(
    val citations: List<Citation> = emptyList(),
)

/**
 * A citation for attributed content in the response.
 *
 * @property startIndex Start index in the response text (inclusive).
 * @property endIndex End index in the response text (exclusive).
 * @property uri URI of the source.
 * @property license License of the cited content.
 */
data class Citation(
    val startIndex: Int? = null,
    val endIndex: Int? = null,
    val uri: String? = null,
    val license: String? = null,
)

/**
 * Token usage statistics for a generation request.
 *
 * @property promptTokenCount Number of tokens in the input prompt.
 * @property candidatesTokenCount Number of tokens in the generated response.
 * @property totalTokenCount Total tokens used (prompt + response).
 */
data class UsageMetadata(
    val promptTokenCount: Int = 0,
    val candidatesTokenCount: Int = 0,
    val totalTokenCount: Int = 0,
)

/**
 * Feedback about the prompt from the model.
 *
 * @property blockReason The reason the prompt was blocked, if applicable.
 * @property safetyRatings Safety ratings for the prompt.
 */
data class PromptFeedback(
    val blockReason: BlockReason? = null,
    val safetyRatings: List<SafetyRating> = emptyList(),
)
