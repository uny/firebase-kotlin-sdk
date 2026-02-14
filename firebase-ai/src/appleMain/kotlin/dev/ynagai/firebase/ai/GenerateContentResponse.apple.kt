package dev.ynagai.firebase.ai

import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSNumber
import swiftPMImport.dev.ynagai.firebase.firebase.ai.KFBBlockReason
import swiftPMImport.dev.ynagai.firebase.firebase.ai.KFBCandidate
import swiftPMImport.dev.ynagai.firebase.firebase.ai.KFBCitation
import swiftPMImport.dev.ynagai.firebase.firebase.ai.KFBCitationMetadata
import swiftPMImport.dev.ynagai.firebase.firebase.ai.KFBFinishReason
import swiftPMImport.dev.ynagai.firebase.firebase.ai.KFBGenerateContentResponse
import swiftPMImport.dev.ynagai.firebase.firebase.ai.KFBHarmCategory
import swiftPMImport.dev.ynagai.firebase.firebase.ai.KFBHarmProbability
import swiftPMImport.dev.ynagai.firebase.firebase.ai.KFBPromptFeedback
import swiftPMImport.dev.ynagai.firebase.firebase.ai.KFBSafetyRating
import swiftPMImport.dev.ynagai.firebase.firebase.ai.KFBUsageMetadata

@OptIn(ExperimentalForeignApi::class)
internal fun KFBGenerateContentResponse.toCommon(): GenerateContentResponse =
    GenerateContentResponse(
        candidates = (candidates() as? List<KFBCandidate>)?.map { it.toCommon() } ?: emptyList(),
        promptFeedback = promptFeedback()?.toCommon(),
        usageMetadata = usageMetadata()?.toCommon(),
    )

@OptIn(ExperimentalForeignApi::class)
internal fun KFBCandidate.toCommon(): Candidate = Candidate(
    content = content().toCommon(),
    finishReason = finishReason()?.toCommonFinishReason(),
    safetyRatings = (safetyRatings() as? List<KFBSafetyRating>)?.map { it.toCommon() } ?: emptyList(),
    citationMetadata = citationMetadata()?.toCommon(),
)

@OptIn(ExperimentalForeignApi::class)
internal fun KFBSafetyRating.toCommon(): SafetyRating = SafetyRating(
    category = category().toCommonHarmCategory(),
    probability = probability().toCommonHarmProbability(),
    blocked = blocked(),
)

@OptIn(ExperimentalForeignApi::class)
private fun KFBHarmCategory.toCommonHarmCategory(): HarmCategory = when (rawValue()) {
    KFBHarmCategory.harassment().rawValue() -> HarmCategory.HARASSMENT
    KFBHarmCategory.hateSpeech().rawValue() -> HarmCategory.HATE_SPEECH
    KFBHarmCategory.sexuallyExplicit().rawValue() -> HarmCategory.SEXUALLY_EXPLICIT
    KFBHarmCategory.dangerousContent().rawValue() -> HarmCategory.DANGEROUS_CONTENT
    KFBHarmCategory.civicIntegrity().rawValue() -> HarmCategory.CIVIC_INTEGRITY
    else -> HarmCategory.UNKNOWN
}

@OptIn(ExperimentalForeignApi::class)
private fun KFBHarmProbability.toCommonHarmProbability(): HarmProbability = when (rawValue()) {
    KFBHarmProbability.negligible().rawValue() -> HarmProbability.NEGLIGIBLE
    KFBHarmProbability.low().rawValue() -> HarmProbability.LOW
    KFBHarmProbability.medium().rawValue() -> HarmProbability.MEDIUM
    KFBHarmProbability.high().rawValue() -> HarmProbability.HIGH
    else -> HarmProbability.UNKNOWN
}

@OptIn(ExperimentalForeignApi::class)
private fun KFBFinishReason.toCommonFinishReason(): FinishReason = when (rawValue()) {
    KFBFinishReason.stop().rawValue() -> FinishReason.STOP
    KFBFinishReason.maxTokens().rawValue() -> FinishReason.MAX_TOKENS
    KFBFinishReason.safety().rawValue() -> FinishReason.SAFETY
    KFBFinishReason.recitation().rawValue() -> FinishReason.RECITATION
    KFBFinishReason.other().rawValue() -> FinishReason.OTHER
    KFBFinishReason.blocklist().rawValue() -> FinishReason.BLOCKLIST
    KFBFinishReason.prohibitedContent().rawValue() -> FinishReason.PROHIBITED_CONTENT
    KFBFinishReason.spii().rawValue() -> FinishReason.SPII
    KFBFinishReason.malformedFunctionCall().rawValue() -> FinishReason.MALFORMED_FUNCTION_CALL
    else -> FinishReason.UNKNOWN
}

@OptIn(ExperimentalForeignApi::class)
private fun KFBBlockReason.toCommonBlockReason(): BlockReason = when (rawValue()) {
    KFBBlockReason.safety().rawValue() -> BlockReason.SAFETY
    KFBBlockReason.other().rawValue() -> BlockReason.OTHER
    KFBBlockReason.blocklist().rawValue() -> BlockReason.BLOCKLIST
    KFBBlockReason.prohibitedContent().rawValue() -> BlockReason.PROHIBITED_CONTENT
    else -> BlockReason.UNKNOWN
}

@OptIn(ExperimentalForeignApi::class)
@Suppress("UNCHECKED_CAST")
internal fun KFBCitationMetadata.toCommon(): CitationMetadata = CitationMetadata(
    citations = (citations() as? List<KFBCitation>)?.map { it.toCommon() } ?: emptyList(),
)

@OptIn(ExperimentalForeignApi::class)
private fun KFBCitation.toCommon(): Citation = Citation(
    startIndex = (startIndex() as? NSNumber)?.intValue,
    endIndex = (endIndex() as? NSNumber)?.intValue,
    uri = uri(),
    license = license(),
)

@OptIn(ExperimentalForeignApi::class)
internal fun KFBUsageMetadata.toCommon(): UsageMetadata = UsageMetadata(
    promptTokenCount = promptTokenCount().toInt(),
    candidatesTokenCount = candidatesTokenCount().toInt(),
    totalTokenCount = totalTokenCount().toInt(),
)

@OptIn(ExperimentalForeignApi::class)
internal fun KFBPromptFeedback.toCommon(): PromptFeedback = PromptFeedback(
    blockReason = blockReason()?.toCommonBlockReason(),
    safetyRatings = (safetyRatings() as? List<KFBSafetyRating>)?.map { it.toCommon() } ?: emptyList(),
)
