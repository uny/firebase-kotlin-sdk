package dev.ynagai.firebase.ai

import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSNumber
import swiftPMImport.dev.ynagai.firebase.firebase.ai.KFBBlockReason
import swiftPMImport.dev.ynagai.firebase.firebase.ai.KFBCandidate
import swiftPMImport.dev.ynagai.firebase.firebase.ai.KFBCitation
import swiftPMImport.dev.ynagai.firebase.firebase.ai.KFBCitationMetadata
import swiftPMImport.dev.ynagai.firebase.firebase.ai.KFBContentModality
import swiftPMImport.dev.ynagai.firebase.firebase.ai.KFBFinishReason
import swiftPMImport.dev.ynagai.firebase.firebase.ai.KFBGenerateContentResponse
import swiftPMImport.dev.ynagai.firebase.firebase.ai.KFBGroundingChunk
import swiftPMImport.dev.ynagai.firebase.firebase.ai.KFBGroundingMetadata
import swiftPMImport.dev.ynagai.firebase.firebase.ai.KFBGroundingSupport
import swiftPMImport.dev.ynagai.firebase.firebase.ai.KFBHarmCategory
import swiftPMImport.dev.ynagai.firebase.firebase.ai.KFBHarmProbability
import swiftPMImport.dev.ynagai.firebase.firebase.ai.KFBModalityTokenCount
import swiftPMImport.dev.ynagai.firebase.firebase.ai.KFBPromptFeedback
import swiftPMImport.dev.ynagai.firebase.firebase.ai.KFBSafetyRating
import swiftPMImport.dev.ynagai.firebase.firebase.ai.KFBURLContextMetadata
import swiftPMImport.dev.ynagai.firebase.firebase.ai.KFBURLMetadata
import swiftPMImport.dev.ynagai.firebase.firebase.ai.KFBURLRetrievalStatus
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
    groundingMetadata = groundingMetadata()?.toCommon(),
    urlContextMetadata = urlContextMetadata()?.toCommon(),
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
@Suppress("UNCHECKED_CAST")
internal fun KFBUsageMetadata.toCommon(): UsageMetadata = UsageMetadata(
    promptTokenCount = promptTokenCount().toInt(),
    candidatesTokenCount = candidatesTokenCount().toInt(),
    totalTokenCount = totalTokenCount().toInt(),
    thoughtsTokenCount = thoughtsTokenCount().toInt(),
    toolUsePromptTokenCount = toolUsePromptTokenCount().toInt(),
    cachedContentTokenCount = cachedContentTokenCount().toInt(),
    promptTokensDetails = (promptTokensDetails() as? List<KFBModalityTokenCount>)?.map { it.toCommon() } ?: emptyList(),
    candidatesTokensDetails = (candidatesTokensDetails() as? List<KFBModalityTokenCount>)?.map { it.toCommon() } ?: emptyList(),
    toolUsePromptTokensDetails = (toolUsePromptTokensDetails() as? List<KFBModalityTokenCount>)?.map { it.toCommon() } ?: emptyList(),
    cacheTokensDetails = (cacheTokensDetails() as? List<KFBModalityTokenCount>)?.map { it.toCommon() } ?: emptyList(),
)

@OptIn(ExperimentalForeignApi::class)
internal fun KFBModalityTokenCount.toCommon(): ModalityTokenCount = ModalityTokenCount(
    modality = modality().toCommon(),
    tokenCount = tokenCount().toInt(),
)

@OptIn(ExperimentalForeignApi::class)
internal fun KFBContentModality.toCommon(): ContentModality = when (rawValue()) {
    KFBContentModality.text().rawValue() -> ContentModality.TEXT
    KFBContentModality.image().rawValue() -> ContentModality.IMAGE
    KFBContentModality.audio().rawValue() -> ContentModality.AUDIO
    KFBContentModality.video().rawValue() -> ContentModality.VIDEO
    KFBContentModality.document().rawValue() -> ContentModality.DOCUMENT
    else -> ContentModality.UNSPECIFIED
}

@OptIn(ExperimentalForeignApi::class)
internal fun KFBPromptFeedback.toCommon(): PromptFeedback = PromptFeedback(
    blockReason = blockReason()?.toCommonBlockReason(),
    safetyRatings = (safetyRatings() as? List<KFBSafetyRating>)?.map { it.toCommon() } ?: emptyList(),
)

@OptIn(ExperimentalForeignApi::class)
@Suppress("UNCHECKED_CAST")
internal fun KFBGroundingMetadata.toCommon(): GroundingMetadata = GroundingMetadata(
    webSearchQueries = (webSearchQueries() as? List<String>) ?: emptyList(),
    searchEntryPoint = searchEntryPoint()?.let { SearchEntryPoint(renderedContent = it.renderedContent()) },
    groundingChunks = (groundingChunks() as? List<KFBGroundingChunk>)?.map { it.toCommon() } ?: emptyList(),
    groundingSupports = (groundingSupports() as? List<KFBGroundingSupport>)?.map { it.toCommon() } ?: emptyList(),
)

@OptIn(ExperimentalForeignApi::class)
internal fun KFBGroundingChunk.toCommon(): GroundingChunk = GroundingChunk(
    web = web()?.let {
        WebGroundingChunk(
            uri = it.uri(),
            title = it.title(),
            domain = it.domain(),
        )
    },
)

@OptIn(ExperimentalForeignApi::class)
@Suppress("UNCHECKED_CAST")
internal fun KFBGroundingSupport.toCommon(): GroundingSupport = GroundingSupport(
    segment = segment().let {
        Segment(
            partIndex = it.partIndex().toInt(),
            startIndex = it.startIndex().toInt(),
            endIndex = it.endIndex().toInt(),
            text = it.text(),
        )
    },
    groundingChunkIndices = (groundingChunkIndices() as? List<NSNumber>)?.map { it.intValue } ?: emptyList(),
)

@OptIn(ExperimentalForeignApi::class)
@Suppress("UNCHECKED_CAST")
internal fun KFBURLContextMetadata.toCommon(): UrlContextMetadata = UrlContextMetadata(
    urlMetadata = (urlMetadata() as? List<KFBURLMetadata>)?.map { it.toCommon() } ?: emptyList(),
)

@OptIn(ExperimentalForeignApi::class)
internal fun KFBURLMetadata.toCommon(): UrlMetadata = UrlMetadata(
    retrievedUrl = retrievedURL()?.absoluteString(),
    retrievalStatus = retrievalStatus().toCommon(),
)

@OptIn(ExperimentalForeignApi::class)
internal fun KFBURLRetrievalStatus.toCommon(): UrlRetrievalStatus = when (rawValue()) {
    KFBURLRetrievalStatus.success().rawValue() -> UrlRetrievalStatus.SUCCESS
    KFBURLRetrievalStatus.error().rawValue() -> UrlRetrievalStatus.ERROR
    KFBURLRetrievalStatus.paywall().rawValue() -> UrlRetrievalStatus.PAYWALL
    KFBURLRetrievalStatus.unsafe().rawValue() -> UrlRetrievalStatus.UNSAFE
    else -> UrlRetrievalStatus.UNSPECIFIED
}
