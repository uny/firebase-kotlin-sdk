package dev.ynagai.firebase.ai

import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSNumber
import swiftPMImport.dev.ynagai.firebase.firebase.ai.KFBGenerationConfig
import swiftPMImport.dev.ynagai.firebase.firebase.ai.KFBImageConfig
import swiftPMImport.dev.ynagai.firebase.firebase.ai.KFBImageConfigAspectRatio
import swiftPMImport.dev.ynagai.firebase.firebase.ai.KFBImageConfigImageSize
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
    imageConfig = imageConfig?.toApple(),
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

@OptIn(ExperimentalForeignApi::class)
internal fun ImageConfig.toApple(): KFBImageConfig = KFBImageConfig(
    aspectRatio = aspectRatio?.toApple(),
    imageSize = imageSize?.toApple(),
)

@OptIn(ExperimentalForeignApi::class)
internal fun AspectRatio.toApple(): KFBImageConfigAspectRatio = when (this) {
    AspectRatio.SQUARE_1x1 -> KFBImageConfigAspectRatio.square1x1()
    AspectRatio.PORTRAIT_2x3 -> KFBImageConfigAspectRatio.portrait2x3()
    AspectRatio.LANDSCAPE_3x2 -> KFBImageConfigAspectRatio.landscape3x2()
    AspectRatio.PORTRAIT_3x4 -> KFBImageConfigAspectRatio.portrait3x4()
    AspectRatio.LANDSCAPE_4x3 -> KFBImageConfigAspectRatio.landscape4x3()
    AspectRatio.PORTRAIT_4x5 -> KFBImageConfigAspectRatio.portrait4x5()
    AspectRatio.LANDSCAPE_5x4 -> KFBImageConfigAspectRatio.landscape5x4()
    AspectRatio.PORTRAIT_9x16 -> KFBImageConfigAspectRatio.portrait9x16()
    AspectRatio.LANDSCAPE_16x9 -> KFBImageConfigAspectRatio.landscape16x9()
    AspectRatio.LANDSCAPE_21x9 -> KFBImageConfigAspectRatio.ultrawide21x9()
}

@OptIn(ExperimentalForeignApi::class)
internal fun ImageSize.toApple(): KFBImageConfigImageSize = when (this) {
    ImageSize.SIZE_512 -> KFBImageConfigImageSize.size512()
    ImageSize.SIZE_1K -> KFBImageConfigImageSize.size1K()
    ImageSize.SIZE_2K -> KFBImageConfigImageSize.size2K()
    ImageSize.SIZE_4K -> KFBImageConfigImageSize.size4K()
}
