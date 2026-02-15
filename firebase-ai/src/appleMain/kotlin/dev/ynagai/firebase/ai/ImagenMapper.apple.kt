package dev.ynagai.firebase.ai

import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSData
import platform.Foundation.NSNumber
import swiftPMImport.dev.ynagai.firebase.firebase.ai.KFBImagenAspectRatio
import swiftPMImport.dev.ynagai.firebase.firebase.ai.KFBImagenGenerationConfig
import swiftPMImport.dev.ynagai.firebase.firebase.ai.KFBImagenGenerationResponse
import swiftPMImport.dev.ynagai.firebase.firebase.ai.KFBImagenImageFormat
import swiftPMImport.dev.ynagai.firebase.firebase.ai.KFBImagenInlineImage
import swiftPMImport.dev.ynagai.firebase.firebase.ai.KFBImagenPersonFilterLevel
import swiftPMImport.dev.ynagai.firebase.firebase.ai.KFBImagenSafetyFilterLevel
import swiftPMImport.dev.ynagai.firebase.firebase.ai.KFBImagenSafetySettings

@OptIn(ExperimentalForeignApi::class)
internal fun ImagenGenerationConfig.toApple(): KFBImagenGenerationConfig = KFBImagenGenerationConfig(
    negativePrompt = negativePrompt,
    numberOfImages = numberOfImages?.let { NSNumber(int = it) },
    aspectRatio = aspectRatio?.toApple(),
    imageFormat = imageFormat?.toApple(),
    addWatermark = addWatermark?.let { NSNumber(bool = it) },
)

@OptIn(ExperimentalForeignApi::class)
internal fun ImagenAspectRatio.toApple(): KFBImagenAspectRatio = when (this) {
    ImagenAspectRatio.SQUARE_1x1 -> KFBImagenAspectRatio.square1x1()
    ImagenAspectRatio.PORTRAIT_9x16 -> KFBImagenAspectRatio.portrait9x16()
    ImagenAspectRatio.LANDSCAPE_16x9 -> KFBImagenAspectRatio.landscape16x9()
    ImagenAspectRatio.PORTRAIT_3x4 -> KFBImagenAspectRatio.portrait3x4()
    ImagenAspectRatio.LANDSCAPE_4x3 -> KFBImagenAspectRatio.landscape4x3()
}

@OptIn(ExperimentalForeignApi::class)
internal fun ImagenImageFormat.toApple(): KFBImagenImageFormat = when (this) {
    is ImagenImageFormat.Png -> KFBImagenImageFormat.png()
    is ImagenImageFormat.Jpeg -> KFBImagenImageFormat.jpegWithCompressionQuality(
        compressionQuality?.let { NSNumber(int = it) },
    )
}

@OptIn(ExperimentalForeignApi::class)
internal fun ImagenSafetyFilterLevel.toApple(): KFBImagenSafetyFilterLevel = when (this) {
    ImagenSafetyFilterLevel.BLOCK_LOW_AND_ABOVE -> KFBImagenSafetyFilterLevel.blockLowAndAbove()
    ImagenSafetyFilterLevel.BLOCK_MEDIUM_AND_ABOVE -> KFBImagenSafetyFilterLevel.blockMediumAndAbove()
    ImagenSafetyFilterLevel.BLOCK_ONLY_HIGH -> KFBImagenSafetyFilterLevel.blockOnlyHigh()
    ImagenSafetyFilterLevel.BLOCK_NONE -> KFBImagenSafetyFilterLevel.blockNone()
}

@OptIn(ExperimentalForeignApi::class)
internal fun ImagenPersonFilterLevel.toApple(): KFBImagenPersonFilterLevel = when (this) {
    ImagenPersonFilterLevel.BLOCK_ALL -> KFBImagenPersonFilterLevel.blockAll()
    ImagenPersonFilterLevel.ALLOW_ADULT -> KFBImagenPersonFilterLevel.allowAdult()
    ImagenPersonFilterLevel.ALLOW_ALL -> KFBImagenPersonFilterLevel.allowAll()
}

@OptIn(ExperimentalForeignApi::class)
internal fun ImagenSafetySettings.toApple(): KFBImagenSafetySettings = KFBImagenSafetySettings(
    safetyFilterLevel = safetyFilterLevel?.toApple(),
    personFilterLevel = personFilterLevel?.toApple(),
)

@OptIn(ExperimentalForeignApi::class)
@Suppress("UNCHECKED_CAST")
internal fun KFBImagenGenerationResponse.toCommon(): ImagenGenerationResponse =
    ImagenGenerationResponse(
        images = (images() as? List<KFBImagenInlineImage>)?.map { it.toCommon() } ?: emptyList(),
        filteredReason = filteredReason(),
    )

@OptIn(ExperimentalForeignApi::class)
internal fun KFBImagenInlineImage.toCommon(): ImagenInlineImage =
    ImagenInlineImage(
        data = (data() as NSData).toByteArray(),
        mimeType = mimeType(),
    )
