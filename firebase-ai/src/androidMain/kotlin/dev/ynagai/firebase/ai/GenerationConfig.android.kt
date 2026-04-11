package dev.ynagai.firebase.ai

import com.google.firebase.ai.type.ResponseModality as AndroidResponseModality
import com.google.firebase.ai.type.generationConfig as androidGenerationConfig
import com.google.firebase.ai.type.thinkingConfig as androidThinkingConfig

internal fun GenerationConfig.toAndroid() = androidGenerationConfig {
    this@toAndroid.temperature?.let { temperature = it }
    this@toAndroid.topP?.let { topP = it }
    this@toAndroid.topK?.let { topK = it }
    this@toAndroid.candidateCount?.let { candidateCount = it }
    this@toAndroid.maxOutputTokens?.let { maxOutputTokens = it }
    this@toAndroid.stopSequences?.let { stopSequences = it }
    this@toAndroid.responseMimeType?.let { responseMimeType = it }
    this@toAndroid.responseSchema?.let { responseSchema = it.toAndroid() }
    this@toAndroid.presencePenalty?.let { presencePenalty = it }
    this@toAndroid.frequencyPenalty?.let { frequencyPenalty = it }
    this@toAndroid.responseModalities?.let {
        responseModalities = it.map { modality -> modality.toAndroid() }
    }
    this@toAndroid.thinkingConfig?.let { thinkingConfig = it.toAndroid() }
}

internal fun ResponseModality.toAndroid(): AndroidResponseModality = when (this) {
    ResponseModality.TEXT -> AndroidResponseModality.TEXT
    ResponseModality.IMAGE -> AndroidResponseModality.IMAGE
    ResponseModality.AUDIO -> AndroidResponseModality.AUDIO
}

internal fun ThinkingConfig.toAndroid() = androidThinkingConfig {
    this@toAndroid.thinkingBudget?.let { thinkingBudget = it }
    this@toAndroid.thinkingLevel?.let {
        thinkingLevel = when (it) {
            ThinkingLevel.MINIMAL -> com.google.firebase.ai.type.ThinkingLevel.MINIMAL
            ThinkingLevel.LOW -> com.google.firebase.ai.type.ThinkingLevel.LOW
            ThinkingLevel.MEDIUM -> com.google.firebase.ai.type.ThinkingLevel.MEDIUM
            ThinkingLevel.HIGH -> com.google.firebase.ai.type.ThinkingLevel.HIGH
        }
    }
    this@toAndroid.includeThoughts?.let { includeThoughts = it }
}
