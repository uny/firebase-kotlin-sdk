package dev.ynagai.firebase.ai

import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSNumber
import swiftPMImport.dev.ynagai.firebase.firebase.ai.KFBGenerationConfig

@OptIn(ExperimentalForeignApi::class)
internal fun GenerationConfig.toApple(): KFBGenerationConfig = KFBGenerationConfig(
    temperature = temperature?.let { NSNumber(float = it) },
    topP = topP?.let { NSNumber(float = it) },
    topK = topK?.let { NSNumber(int = it) },
    candidateCount = candidateCount?.let { NSNumber(int = it) },
    maxOutputTokens = maxOutputTokens?.let { NSNumber(int = it) },
    presencePenalty = null,
    frequencyPenalty = null,
    stopSequences = stopSequences,
    responseMIMEType = responseMimeType,
)
