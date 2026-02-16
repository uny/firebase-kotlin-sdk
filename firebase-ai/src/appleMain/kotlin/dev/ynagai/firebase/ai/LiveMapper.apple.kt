package dev.ynagai.firebase.ai

import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSData
import platform.Foundation.NSJSONSerialization
import platform.Foundation.NSNumber
import swiftPMImport.dev.ynagai.firebase.firebase.ai.KFBFunctionCallPart
import swiftPMImport.dev.ynagai.firebase.firebase.ai.KFBFunctionResponsePart
import swiftPMImport.dev.ynagai.firebase.firebase.ai.KFBInlineDataPart
import swiftPMImport.dev.ynagai.firebase.firebase.ai.KFBLiveGenerationConfig
import swiftPMImport.dev.ynagai.firebase.firebase.ai.KFBLiveServerMessage
import swiftPMImport.dev.ynagai.firebase.firebase.ai.KFBResponseModality
import swiftPMImport.dev.ynagai.firebase.firebase.ai.KFBSpeechConfig
import swiftPMImport.dev.ynagai.firebase.firebase.ai.KFBTextPart

@OptIn(ExperimentalForeignApi::class)
internal fun LiveGenerationConfig.toApple(): KFBLiveGenerationConfig = KFBLiveGenerationConfig(
    temperature = temperature?.let { NSNumber(float = it) },
    topP = topP?.let { NSNumber(float = it) },
    topK = topK?.let { NSNumber(int = it) },
    candidateCount = null,
    maxOutputTokens = maxOutputTokens?.let { NSNumber(int = it) },
    presencePenalty = null,
    frequencyPenalty = null,
    responseModalities = responseModality?.let { listOf(it.toAppleResponseModality()) },
    speech = speechConfig?.toApple(),
    inputAudioTranscription = null,
    outputAudioTranscription = null,
)

@OptIn(ExperimentalForeignApi::class)
internal fun ResponseModality.toAppleResponseModality(): KFBResponseModality = when (this) {
    ResponseModality.TEXT -> KFBResponseModality.text()
    ResponseModality.IMAGE -> KFBResponseModality.image()
    ResponseModality.AUDIO -> throw UnsupportedOperationException("Audio modality is not supported yet")
}

@OptIn(ExperimentalForeignApi::class)
internal fun SpeechConfig.toApple(): KFBSpeechConfig =
    KFBSpeechConfig(voiceName = voice.name, languageCode = null)

@OptIn(ExperimentalForeignApi::class)
internal fun FunctionResponsePart.toAppleFunctionResponse(): KFBFunctionResponsePart =
    KFBFunctionResponsePart(
        name = name,
        response = response as Map<Any?, *>,
    )

@OptIn(ExperimentalForeignApi::class)
@Suppress("UNCHECKED_CAST")
internal fun KFBLiveServerMessage.toCommon(): LiveServerMessage {
    // Use nullable property accessors to determine message type
    content()?.let { serverContent ->
        val modelTurn = serverContent.modelTurn()
        val parts = modelTurn?.parts()?.mapNotNull { part ->
            when (part) {
                is KFBTextPart -> TextPart(text = part.text())
                is KFBInlineDataPart -> InlineDataPart(
                    mimeType = part.mimeType(),
                    data = (part.data() as? NSData)?.toByteArray() ?: byteArrayOf(),
                )
                is KFBFunctionCallPart -> {
                    val args = part.argsData()?.let { data ->
                        NSJSONSerialization.JSONObjectWithData(data, 0u, null) as? Map<String, Any?>
                    } ?: emptyMap()
                    FunctionCallPart(name = part.name(), args = args)
                }
                else -> null
            }
        } ?: emptyList()
        val content = if (parts.isNotEmpty()) {
            Content(role = modelTurn?.role(), parts = parts)
        } else {
            null
        }
        return LiveServerMessage.Content(
            content = content,
            isTurnComplete = serverContent.isTurnComplete(),
            wasInterrupted = serverContent.wasInterrupted(),
        )
    }

    toolCall()?.let { serverToolCall ->
        val calls = (serverToolCall.functionCalls() as? List<KFBFunctionCallPart>)?.map { call ->
            val args = call.argsData()?.let { data ->
                NSJSONSerialization.JSONObjectWithData(data, 0u, null) as? Map<String, Any?>
            } ?: emptyMap()
            FunctionCallPart(name = call.name(), args = args)
        } ?: emptyList()
        return LiveServerMessage.ToolCall(functionCalls = calls)
    }

    toolCallCancellation()?.let { cancellation ->
        return LiveServerMessage.ToolCallCancellation(
            ids = (cancellation.ids() as? List<String>) ?: emptyList(),
        )
    }

    goingAwayNotice()?.let {
        return LiveServerMessage.GoingAway
    }

    throw IllegalArgumentException("Unknown KFBLiveServerMessage type")
}
