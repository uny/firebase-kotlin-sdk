package dev.ynagai.firebase.ai

data class GenerateContentResponse(
    val candidates: List<Candidate> = emptyList(),
    val promptFeedback: PromptFeedback? = null,
    val usageMetadata: UsageMetadata? = null,
) {
    val text: String?
        get() = candidates.firstOrNull()?.content?.parts
            ?.filterIsInstance<TextPart>()
            ?.joinToString("") { it.text }
            ?.takeIf { it.isNotEmpty() }
}

data class Candidate(
    val content: Content = Content(),
    val finishReason: FinishReason? = null,
    val safetyRatings: List<SafetyRating> = emptyList(),
    val citationMetadata: CitationMetadata? = null,
)

data class SafetyRating(
    val category: HarmCategory,
    val probability: HarmProbability,
    val blocked: Boolean = false,
)

data class CitationMetadata(
    val citations: List<Citation> = emptyList(),
)

data class Citation(
    val startIndex: Int? = null,
    val endIndex: Int? = null,
    val uri: String? = null,
    val license: String? = null,
)

data class UsageMetadata(
    val promptTokenCount: Int = 0,
    val candidatesTokenCount: Int = 0,
    val totalTokenCount: Int = 0,
)

data class PromptFeedback(
    val blockReason: BlockReason? = null,
    val safetyRatings: List<SafetyRating> = emptyList(),
)
