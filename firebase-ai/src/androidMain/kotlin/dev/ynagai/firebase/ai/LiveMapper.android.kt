@file:OptIn(PublicPreviewAPI::class)

package dev.ynagai.firebase.ai

import com.google.firebase.ai.type.InlineData as AndroidInlineData
import com.google.firebase.ai.type.LiveGenerationConfig as AndroidLiveGenerationConfig
import com.google.firebase.ai.type.LiveServerContent as AndroidLiveServerContent
import com.google.firebase.ai.type.LiveServerGoAway as AndroidLiveServerGoAway
import com.google.firebase.ai.type.LiveServerMessage as AndroidLiveServerMessage
import com.google.firebase.ai.type.LiveServerSetupComplete as AndroidLiveServerSetupComplete
import com.google.firebase.ai.type.LiveServerToolCall as AndroidLiveServerToolCall
import com.google.firebase.ai.type.LiveServerToolCallCancellation as AndroidLiveServerToolCallCancellation
import com.google.firebase.ai.type.LiveSessionResumptionUpdate as AndroidLiveSessionResumptionUpdate
import com.google.firebase.ai.type.PublicPreviewAPI
import com.google.firebase.ai.type.ContextWindowCompressionConfig as AndroidContextWindowCompressionConfig
import com.google.firebase.ai.type.SlidingWindow as AndroidSlidingWindow
import com.google.firebase.ai.type.SessionResumptionConfig as AndroidSessionResumptionConfig
import com.google.firebase.ai.type.AudioTranscriptionConfig as AndroidAudioTranscriptionConfig
import com.google.firebase.ai.type.SpeechConfig as AndroidSpeechConfig
import com.google.firebase.ai.type.Voice as AndroidVoice
import com.google.firebase.ai.type.liveGenerationConfig as androidLiveGenerationConfig

internal fun LiveGenerationConfig.toAndroid(): AndroidLiveGenerationConfig =
    androidLiveGenerationConfig {
        this@toAndroid.responseModality?.let { responseModality = it.toAndroid() }
        this@toAndroid.speechConfig?.let { speechConfig = it.toAndroid() }
        this@toAndroid.temperature?.let { temperature = it }
        this@toAndroid.topK?.let { topK = it }
        this@toAndroid.topP?.let { topP = it }
        this@toAndroid.maxOutputTokens?.let { maxOutputTokens = it }
        this@toAndroid.inputAudioTranscription?.let {
            inputAudioTranscription = AndroidAudioTranscriptionConfig()
        }
        this@toAndroid.outputAudioTranscription?.let {
            outputAudioTranscription = AndroidAudioTranscriptionConfig()
        }
        this@toAndroid.contextWindowCompression?.let {
            contextWindowCompression = it.toAndroid()
        }
    }

internal fun ContextWindowCompressionConfig.toAndroid() = AndroidContextWindowCompressionConfig(
    triggerTokens = triggerTokens,
    slidingWindow = slidingWindow?.let { AndroidSlidingWindow(targetTokens = it.targetTokens) },
)

internal fun SessionResumptionConfig.toAndroid() = if (handle != null) {
    AndroidSessionResumptionConfig(handle)
} else {
    AndroidSessionResumptionConfig()
}

internal fun SpeechConfig.toAndroid(): AndroidSpeechConfig =
    AndroidSpeechConfig(voice = voice.toAndroid())

internal fun Voice.toAndroid(): AndroidVoice =
    AndroidVoice(voiceName = name)

internal fun InlineDataPart.toAndroidInlineData(): AndroidInlineData =
    AndroidInlineData(data = data, mimeType = mimeType)

internal fun AndroidLiveServerMessage.toCommon(): LiveServerMessage = when (this) {
    is AndroidLiveServerContent -> {
        LiveServerMessage.Content(
            content = content?.toCommon(),
            isTurnComplete = turnComplete,
            wasInterrupted = interrupted,
            isGenerationComplete = generationComplete,
            inputTranscription = inputTranscription?.text,
            outputTranscription = outputTranscription?.text,
        )
    }
    is AndroidLiveServerToolCall -> {
        LiveServerMessage.ToolCall(
            functionCalls = functionCalls.map { it.toCommon() },
        )
    }
    is AndroidLiveServerToolCallCancellation -> {
        LiveServerMessage.ToolCallCancellation(ids = functionIds)
    }
    is AndroidLiveServerGoAway -> {
        LiveServerMessage.GoingAway
    }
    is AndroidLiveServerSetupComplete -> {
        // Setup complete is an internal message; map to a content with no data
        LiveServerMessage.Content(content = null)
    }
    is AndroidLiveSessionResumptionUpdate -> {
        LiveServerMessage.SessionResumptionUpdate(
            newHandle = newHandle,
            resumable = resumable,
        )
    }
    else -> throw IllegalArgumentException("Unknown LiveServerMessage type: ${this::class.simpleName}")
}
