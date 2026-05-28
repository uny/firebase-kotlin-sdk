package dev.ynagai.firebase.ai

import com.google.firebase.ai.type.BlockReason as AndroidBlockReason
import com.google.firebase.ai.type.Candidate as AndroidCandidate
import com.google.firebase.ai.type.Citation as AndroidCitation
import com.google.firebase.ai.type.CitationMetadata as AndroidCitationMetadata
import com.google.firebase.ai.type.ContentModality as AndroidContentModality
import com.google.firebase.ai.type.FinishReason as AndroidFinishReason
import com.google.firebase.ai.type.GenerateContentResponse as AndroidGenerateContentResponse
import com.google.firebase.ai.type.GroundingMetadata as AndroidGroundingMetadata
import com.google.firebase.ai.type.GroundingSupport as AndroidGroundingSupport
import com.google.firebase.ai.type.HarmCategory as AndroidHarmCategory
import com.google.firebase.ai.type.HarmProbability as AndroidHarmProbability
import com.google.firebase.ai.type.ModalityTokenCount as AndroidModalityTokenCount
import com.google.firebase.ai.type.PromptFeedback as AndroidPromptFeedback
import com.google.firebase.ai.type.SafetyRating as AndroidSafetyRating
import com.google.firebase.ai.type.Segment as AndroidSegment
import com.google.firebase.ai.type.UrlContextMetadata as AndroidUrlContextMetadata
import com.google.firebase.ai.type.UrlMetadata as AndroidUrlMetadata
import com.google.firebase.ai.type.UrlRetrievalStatus as AndroidUrlRetrievalStatus
import com.google.firebase.ai.type.UsageMetadata as AndroidUsageMetadata
import com.google.firebase.ai.type.WebGroundingChunk as AndroidWebGroundingChunk

internal fun AndroidGenerateContentResponse.toCommon(): GenerateContentResponse =
    GenerateContentResponse(
        candidates = candidates.map { it.toCommon() },
        promptFeedback = promptFeedback?.toCommon(),
        usageMetadata = usageMetadata?.toCommon(),
    )

internal fun AndroidCandidate.toCommon(): Candidate = Candidate(
    content = content.toCommon(),
    finishReason = finishReason?.toCommon(),
    finishMessage = finishMessage,
    safetyRatings = safetyRatings.map { it.toCommon() },
    citationMetadata = citationMetadata?.toCommon(),
    groundingMetadata = groundingMetadata?.toCommon(),
    urlContextMetadata = urlContextMetadata?.toCommon(),
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
    AndroidHarmCategory.IMAGE_HATE -> HarmCategory.IMAGE_HATE
    AndroidHarmCategory.IMAGE_DANGEROUS_CONTENT -> HarmCategory.IMAGE_DANGEROUS_CONTENT
    AndroidHarmCategory.IMAGE_HARASSMENT -> HarmCategory.IMAGE_HARASSMENT
    AndroidHarmCategory.IMAGE_SEXUALLY_EXPLICIT -> HarmCategory.IMAGE_SEXUALLY_EXPLICIT
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
    AndroidFinishReason.IMAGE_SAFETY -> FinishReason.IMAGE_SAFETY
    AndroidFinishReason.IMAGE_PROHIBITED_CONTENT -> FinishReason.IMAGE_PROHIBITED_CONTENT
    AndroidFinishReason.IMAGE_OTHER -> FinishReason.IMAGE_OTHER
    AndroidFinishReason.NO_IMAGE -> FinishReason.NO_IMAGE
    AndroidFinishReason.IMAGE_RECITATION -> FinishReason.IMAGE_RECITATION
    AndroidFinishReason.LANGUAGE -> FinishReason.LANGUAGE
    AndroidFinishReason.UNEXPECTED_TOOL_CALL -> FinishReason.UNEXPECTED_TOOL_CALL
    AndroidFinishReason.TOO_MANY_TOOL_CALLS -> FinishReason.TOO_MANY_TOOL_CALLS
    AndroidFinishReason.MISSING_THOUGHT_SIGNATURE -> FinishReason.MISSING_THOUGHT_SIGNATURE
    AndroidFinishReason.MALFORMED_RESPONSE -> FinishReason.MALFORMED_RESPONSE
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
    promptTokenCount = promptTokenCount,
    candidatesTokenCount = candidatesTokenCount ?: 0,
    totalTokenCount = totalTokenCount,
    thoughtsTokenCount = thoughtsTokenCount,
    toolUsePromptTokenCount = toolUsePromptTokenCount,
    cachedContentTokenCount = cachedContentTokenCount,
    promptTokensDetails = promptTokensDetails.map { it.toCommon() },
    candidatesTokensDetails = candidatesTokensDetails.map { it.toCommon() },
    toolUsePromptTokensDetails = toolUsePromptTokensDetails.map { it.toCommon() },
    cacheTokensDetails = cacheTokensDetails.map { it.toCommon() },
)

internal fun AndroidModalityTokenCount.toCommon(): ModalityTokenCount = ModalityTokenCount(
    modality = modality.toCommon(),
    tokenCount = tokenCount,
)

internal fun AndroidContentModality.toCommon(): ContentModality = when (this) {
    AndroidContentModality.TEXT -> ContentModality.TEXT
    AndroidContentModality.IMAGE -> ContentModality.IMAGE
    AndroidContentModality.AUDIO -> ContentModality.AUDIO
    AndroidContentModality.VIDEO -> ContentModality.VIDEO
    AndroidContentModality.DOCUMENT -> ContentModality.DOCUMENT
    else -> ContentModality.UNSPECIFIED
}

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

internal fun AndroidGroundingMetadata.toCommon(): GroundingMetadata = GroundingMetadata(
    webSearchQueries = webSearchQueries,
    searchEntryPoint = searchEntryPoint?.let { SearchEntryPoint(renderedContent = it.renderedContent) },
    groundingChunks = groundingChunks.map { chunk ->
        GroundingChunk(
            web = chunk.web?.toCommon(),
            maps = chunk.maps?.let {
                GoogleMapsGroundingChunk(uri = it.uri, title = it.title, placeId = it.placeId)
            },
        )
    },
    groundingSupports = groundingSupports.map { it.toCommon() },
)

internal fun AndroidWebGroundingChunk.toCommon(): WebGroundingChunk = WebGroundingChunk(
    uri = uri,
    title = title,
    domain = domain,
)

internal fun AndroidGroundingSupport.toCommon(): GroundingSupport = GroundingSupport(
    segment = segment.toCommon(),
    groundingChunkIndices = groundingChunkIndices,
)

internal fun AndroidSegment.toCommon(): Segment = Segment(
    partIndex = partIndex,
    startIndex = startIndex,
    endIndex = endIndex,
    text = text,
)

internal fun AndroidUrlContextMetadata.toCommon(): UrlContextMetadata = UrlContextMetadata(
    urlMetadata = urlMetadata.map { it.toCommon() },
)

internal fun AndroidUrlMetadata.toCommon(): UrlMetadata = UrlMetadata(
    retrievedUrl = retrievedUrl,
    retrievalStatus = urlRetrievalStatus.toCommon(),
)

internal fun AndroidUrlRetrievalStatus.toCommon(): UrlRetrievalStatus = when (this) {
    AndroidUrlRetrievalStatus.SUCCESS -> UrlRetrievalStatus.SUCCESS
    AndroidUrlRetrievalStatus.ERROR -> UrlRetrievalStatus.ERROR
    AndroidUrlRetrievalStatus.PAYWALL -> UrlRetrievalStatus.PAYWALL
    AndroidUrlRetrievalStatus.UNSAFE -> UrlRetrievalStatus.UNSAFE
    else -> UrlRetrievalStatus.UNSPECIFIED
}
