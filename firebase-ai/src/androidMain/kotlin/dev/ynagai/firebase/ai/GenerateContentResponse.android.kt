package dev.ynagai.firebase.ai

import com.google.firebase.ai.type.BlockReason as AndroidBlockReason
import com.google.firebase.ai.type.Candidate as AndroidCandidate
import com.google.firebase.ai.type.Citation as AndroidCitation
import com.google.firebase.ai.type.CitationMetadata as AndroidCitationMetadata
import com.google.firebase.ai.type.FinishReason as AndroidFinishReason
import com.google.firebase.ai.type.GenerateContentResponse as AndroidGenerateContentResponse
import com.google.firebase.ai.type.HarmCategory as AndroidHarmCategory
import com.google.firebase.ai.type.HarmProbability as AndroidHarmProbability
import com.google.firebase.ai.type.PromptFeedback as AndroidPromptFeedback
import com.google.firebase.ai.type.SafetyRating as AndroidSafetyRating
import com.google.firebase.ai.type.UsageMetadata as AndroidUsageMetadata

internal fun AndroidGenerateContentResponse.toCommon(): GenerateContentResponse =
    GenerateContentResponse(
        candidates = candidates.map { it.toCommon() },
        promptFeedback = promptFeedback?.toCommon(),
        usageMetadata = usageMetadata?.toCommon(),
    )

internal fun AndroidCandidate.toCommon(): Candidate = Candidate(
    content = content.toCommon(),
    finishReason = finishReason?.toCommon(),
    safetyRatings = safetyRatings.map { it.toCommon() },
    citationMetadata = citationMetadata?.toCommon(),
)

internal fun AndroidSafetyRating.toCommon(): SafetyRating = SafetyRating(
    category = category.toCommon(),
    probability = probability.toCommon(),
    blocked = blocked ?: false,
)

internal fun AndroidHarmCategory.toCommon(): HarmCategory = when (this) {
    AndroidHarmCategory.UNKNOWN -> HarmCategory.UNKNOWN
    AndroidHarmCategory.HARASSMENT -> HarmCategory.HARASSMENT
    AndroidHarmCategory.HATE_SPEECH -> HarmCategory.HATE_SPEECH
    AndroidHarmCategory.SEXUALLY_EXPLICIT -> HarmCategory.SEXUALLY_EXPLICIT
    AndroidHarmCategory.DANGEROUS_CONTENT -> HarmCategory.DANGEROUS_CONTENT
    AndroidHarmCategory.CIVIC_INTEGRITY -> HarmCategory.CIVIC_INTEGRITY
    else -> HarmCategory.UNKNOWN
}

internal fun AndroidHarmProbability.toCommon(): HarmProbability = when (this) {
    AndroidHarmProbability.UNKNOWN -> HarmProbability.UNKNOWN
    AndroidHarmProbability.NEGLIGIBLE -> HarmProbability.NEGLIGIBLE
    AndroidHarmProbability.LOW -> HarmProbability.LOW
    AndroidHarmProbability.MEDIUM -> HarmProbability.MEDIUM
    AndroidHarmProbability.HIGH -> HarmProbability.HIGH
    else -> HarmProbability.UNKNOWN
}

internal fun AndroidFinishReason.toCommon(): FinishReason = when (this) {
    AndroidFinishReason.UNKNOWN -> FinishReason.UNKNOWN
    AndroidFinishReason.STOP -> FinishReason.STOP
    AndroidFinishReason.MAX_TOKENS -> FinishReason.MAX_TOKENS
    AndroidFinishReason.SAFETY -> FinishReason.SAFETY
    AndroidFinishReason.RECITATION -> FinishReason.RECITATION
    AndroidFinishReason.OTHER -> FinishReason.OTHER
    AndroidFinishReason.BLOCKLIST -> FinishReason.BLOCKLIST
    AndroidFinishReason.PROHIBITED_CONTENT -> FinishReason.PROHIBITED_CONTENT
    AndroidFinishReason.SPII -> FinishReason.SPII
    AndroidFinishReason.MALFORMED_FUNCTION_CALL -> FinishReason.MALFORMED_FUNCTION_CALL
    else -> FinishReason.UNKNOWN
}

internal fun AndroidCitationMetadata.toCommon(): CitationMetadata = CitationMetadata(
    citations = citations.map { it.toCommon() },
)

internal fun AndroidCitation.toCommon(): Citation = Citation(
    startIndex = startIndex,
    endIndex = endIndex,
    uri = uri,
    license = license,
)

internal fun AndroidUsageMetadata.toCommon(): UsageMetadata = UsageMetadata(
    promptTokenCount = promptTokenCount ?: 0,
    candidatesTokenCount = candidatesTokenCount ?: 0,
    totalTokenCount = totalTokenCount ?: 0,
)

internal fun AndroidPromptFeedback.toCommon(): PromptFeedback = PromptFeedback(
    blockReason = blockReason?.toCommon(),
    safetyRatings = safetyRatings.map { it.toCommon() },
)

internal fun AndroidBlockReason.toCommon(): BlockReason = when (this) {
    AndroidBlockReason.UNKNOWN -> BlockReason.UNKNOWN
    AndroidBlockReason.SAFETY -> BlockReason.SAFETY
    AndroidBlockReason.OTHER -> BlockReason.OTHER
    AndroidBlockReason.BLOCKLIST -> BlockReason.BLOCKLIST
    AndroidBlockReason.PROHIBITED_CONTENT -> BlockReason.PROHIBITED_CONTENT
    else -> BlockReason.UNKNOWN
}
