package dev.ynagai.firebase.ai

import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSNumber
import swiftPMImport.dev.ynagai.firebase.firebase.ai.KFBFunctionCallPart
import swiftPMImport.dev.ynagai.firebase.firebase.ai.KFBFunctionResponsePart
import swiftPMImport.dev.ynagai.firebase.firebase.ai.KFBAudioTranscriptionConfig
import swiftPMImport.dev.ynagai.firebase.firebase.ai.KFBContextWindowCompressionConfig
import swiftPMImport.dev.ynagai.firebase.firebase.ai.KFBLiveGenerationConfig
import swiftPMImport.dev.ynagai.firebase.firebase.ai.KFBLiveServerMessage
import swiftPMImport.dev.ynagai.firebase.firebase.ai.KFBResponseModality
import swiftPMImport.dev.ynagai.firebase.firebase.ai.KFBSessionResumptionConfig
import swiftPMImport.dev.ynagai.firebase.firebase.ai.KFBSlidingWindow
import swiftPMImport.dev.ynagai.firebase.firebase.ai.KFBSpeechConfig

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
    inputAudioTranscription = inputAudioTranscription?.let { KFBAudioTranscriptionConfig() },
    outputAudioTranscription = outputAudioTranscription?.let { KFBAudioTranscriptionConfig() },
    contextWindowCompression = contextWindowCompression?.toApple(),
)

@OptIn(ExperimentalForeignApi::class)
internal fun ContextWindowCompressionConfig.toApple(): KFBContextWindowCompressionConfig =
    KFBContextWindowCompressionConfig(
        triggerTokens = triggerTokens?.let { NSNumber(int = it) },
        slidingWindow = slidingWindow?.let {
            KFBSlidingWindow(targetTokens = it.targetTokens?.let { t -> NSNumber(int = t) })
        },
    )

@OptIn(ExperimentalForeignApi::class)
internal fun SessionResumptionConfig.toApple(): KFBSessionResumptionConfig =
    if (handle != null) {
        KFBSessionResumptionConfig(handle = handle)
    } else {
        KFBSessionResumptionConfig()
    }

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
        functionId = id,
    )

@OptIn(ExperimentalForeignApi::class)
@Suppress("UNCHECKED_CAST")
internal fun KFBLiveServerMessage.toCommon(): LiveServerMessage {
    // Use nullable property accessors to determine message type
    content()?.let { serverContent ->
        // Reuse the shared KFBModelContent mapper so every part type (and id/isThought) is
        // preserved consistently; keep the null-when-empty semantics for content.
        val content = serverContent.modelTurn()?.toCommon()?.takeIf { it.parts.isNotEmpty() }
        return LiveServerMessage.Content(
            content = content,
            isTurnComplete = serverContent.isTurnComplete(),
            wasInterrupted = serverContent.wasInterrupted(),
            isGenerationComplete = serverContent.isGenerationComplete(),
            inputTranscription = serverContent.inputAudioTranscription()?.text(),
            outputTranscription = serverContent.outputAudioTranscription()?.text(),
        )
    }

    toolCall()?.let { serverToolCall ->
        val calls = (serverToolCall.functionCalls() as? List<KFBFunctionCallPart>)
            ?.map { it.toCommon() } ?: emptyList()
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

    sessionResumptionUpdate()?.let { update ->
        return LiveServerMessage.SessionResumptionUpdate(
            newHandle = update.newHandle(),
            resumable = update.resumable(),
        )
    }

    throw IllegalArgumentException("Unknown KFBLiveServerMessage type")
}
