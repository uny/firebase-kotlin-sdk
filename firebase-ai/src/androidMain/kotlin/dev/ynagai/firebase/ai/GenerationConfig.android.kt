package dev.ynagai.firebase.ai

import com.google.firebase.ai.type.generationConfig as androidGenerationConfig

internal fun GenerationConfig.toAndroid() = androidGenerationConfig {
    this@toAndroid.temperature?.let { temperature = it }
    this@toAndroid.topP?.let { topP = it }
    this@toAndroid.topK?.let { topK = it }
    this@toAndroid.candidateCount?.let { candidateCount = it }
    this@toAndroid.maxOutputTokens?.let { maxOutputTokens = it }
    this@toAndroid.stopSequences?.let { stopSequences = it }
    this@toAndroid.responseMimeType?.let { responseMimeType = it }
}
