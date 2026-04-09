package dev.ynagai.firebase.ai

import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSNumber
import swiftPMImport.dev.ynagai.firebase.firebase.ai.KFBGenerationConfig
import swiftPMImport.dev.ynagai.firebase.firebase.ai.KFBThinkingConfig
import swiftPMImport.dev.ynagai.firebase.firebase.ai.KFBThinkingLevel

@OptIn(ExperimentalForeignApi::class)
internal fun GenerationConfig.toApple(): KFBGenerationConfig = KFBGenerationConfig(
    temperature = temperature?.let { NSNumber(float = it) },
    topP = topP?.let { NSNumber(float = it) },
    topK = topK?.let { NSNumber(int = it) },
    candidateCount = candidateCount?.let { NSNumber(int = it) },
    maxOutputTokens = maxOutputTokens?.let { NSNumber(int = it) },
    presencePenalty = presencePenalty?.let { NSNumber(float = it) },
    frequencyPenalty = frequencyPenalty?.let { NSNumber(float = it) },
    stopSequences = stopSequences,
    responseMIMEType = responseMimeType,
    responseSchema = responseSchema?.toApple(),
    responseModalities = responseModalities?.map { it.name },
    thinkingConfig = thinkingConfig?.toApple(),
)

@OptIn(ExperimentalForeignApi::class)
internal fun ThinkingConfig.toApple(): KFBThinkingConfig = when {
    thinkingLevel != null -> KFBThinkingConfig(
        thinkingLevel = thinkingLevel.toApple(),
        includeThoughts = includeThoughts?.let { NSNumber(bool = it) },
    )
    else -> KFBThinkingConfig(
        thinkingBudget = thinkingBudget?.let { NSNumber(int = it) },
        includeThoughts = includeThoughts?.let { NSNumber(bool = it) },
    )
}

@OptIn(ExperimentalForeignApi::class)
internal fun ThinkingLevel.toApple(): KFBThinkingLevel = when (this) {
    ThinkingLevel.MINIMAL -> KFBThinkingLevel.minimal()
    ThinkingLevel.LOW -> KFBThinkingLevel.low()
    ThinkingLevel.MEDIUM -> KFBThinkingLevel.medium()
    ThinkingLevel.HIGH -> KFBThinkingLevel.high()
}
