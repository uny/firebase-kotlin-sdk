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
import com.google.firebase.ai.type.PublicPreviewAPI
import com.google.firebase.ai.type.SpeechConfig as AndroidSpeechConfig
import com.google.firebase.ai.type.Voice as AndroidVoice
import com.google.firebase.ai.type.liveGenerationConfig as androidLiveGenerationConfig
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.longOrNull

internal fun LiveGenerationConfig.toAndroid(): AndroidLiveGenerationConfig =
    androidLiveGenerationConfig {
        this@toAndroid.responseModality?.let { responseModality = it.toAndroid() }
        this@toAndroid.speechConfig?.let { speechConfig = it.toAndroid() }
        this@toAndroid.temperature?.let { temperature = it }
        this@toAndroid.topK?.let { topK = it }
        this@toAndroid.topP?.let { topP = it }
        this@toAndroid.maxOutputTokens?.let { maxOutputTokens = it }
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
        )
    }
    is AndroidLiveServerToolCall -> {
        LiveServerMessage.ToolCall(
            functionCalls = functionCalls.map { call ->
                FunctionCallPart(
                    name = call.name,
                    args = call.args.mapValues { (_, v) -> v.toAny() },
                )
            },
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
    else -> throw IllegalArgumentException("Unknown LiveServerMessage type: ${this::class.simpleName}")
}

private fun JsonElement.toAny(): Any? = when (this) {
    is JsonNull -> null
    is JsonPrimitive -> if (isString) content else booleanOrNull ?: longOrNull ?: doubleOrNull ?: content
    is JsonObject -> mapValues { (_, v) -> v.toAny() }
    is JsonArray -> map { it.toAny() }
}
