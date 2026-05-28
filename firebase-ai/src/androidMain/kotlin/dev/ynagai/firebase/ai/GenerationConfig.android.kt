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
    this@toAndroid.imageConfig?.let { imageConfig = it.toAndroid() }
}

internal fun ImageConfig.toAndroid() = com.google.firebase.ai.type.imageConfig {
    this@toAndroid.aspectRatio?.let {
        aspectRatio = when (it) {
            AspectRatio.SQUARE_1x1 -> com.google.firebase.ai.type.AspectRatio.SQUARE_1x1
            AspectRatio.PORTRAIT_2x3 -> com.google.firebase.ai.type.AspectRatio.PORTRAIT_2x3
            AspectRatio.LANDSCAPE_3x2 -> com.google.firebase.ai.type.AspectRatio.LANDSCAPE_3x2
            AspectRatio.PORTRAIT_3x4 -> com.google.firebase.ai.type.AspectRatio.PORTRAIT_3x4
            AspectRatio.LANDSCAPE_4x3 -> com.google.firebase.ai.type.AspectRatio.LANDSCAPE_4x3
            AspectRatio.PORTRAIT_4x5 -> com.google.firebase.ai.type.AspectRatio.PORTRAIT_4x5
            AspectRatio.LANDSCAPE_5x4 -> com.google.firebase.ai.type.AspectRatio.LANDSCAPE_5x4
            AspectRatio.PORTRAIT_9x16 -> com.google.firebase.ai.type.AspectRatio.PORTRAIT_9x16
            AspectRatio.LANDSCAPE_16x9 -> com.google.firebase.ai.type.AspectRatio.LANDSCAPE_16x9
            AspectRatio.LANDSCAPE_21x9 -> com.google.firebase.ai.type.AspectRatio.LANDSCAPE_21x9
        }
    }
    this@toAndroid.imageSize?.let {
        imageSize = when (it) {
            ImageSize.SIZE_512 -> com.google.firebase.ai.type.ImageSize.SIZE_512
            ImageSize.SIZE_1K -> com.google.firebase.ai.type.ImageSize.SIZE_1K
            ImageSize.SIZE_2K -> com.google.firebase.ai.type.ImageSize.SIZE_2K
            ImageSize.SIZE_4K -> com.google.firebase.ai.type.ImageSize.SIZE_4K
        }
    }
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
