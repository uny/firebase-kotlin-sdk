package dev.ynagai.firebase.ai

import com.google.firebase.ai.type.ImagenAspectRatio as AndroidImagenAspectRatio
import com.google.firebase.ai.type.ImagenGenerationConfig as AndroidImagenGenerationConfig
import com.google.firebase.ai.type.ImagenGenerationResponse as AndroidImagenGenerationResponse
import com.google.firebase.ai.type.ImagenImageFormat as AndroidImagenImageFormat
import com.google.firebase.ai.type.ImagenInlineImage as AndroidImagenInlineImage
import com.google.firebase.ai.type.ImagenPersonFilterLevel as AndroidImagenPersonFilterLevel
import com.google.firebase.ai.type.ImagenSafetyFilterLevel as AndroidImagenSafetyFilterLevel
import com.google.firebase.ai.type.ImagenSafetySettings as AndroidImagenSafetySettings
import com.google.firebase.ai.type.imagenGenerationConfig as androidImagenGenerationConfig

internal fun ImagenGenerationConfig.toAndroid(): AndroidImagenGenerationConfig =
    androidImagenGenerationConfig {
        this@toAndroid.negativePrompt?.let { negativePrompt = it }
        this@toAndroid.numberOfImages?.let { numberOfImages = it }
        this@toAndroid.aspectRatio?.let { aspectRatio = it.toAndroid() }
        this@toAndroid.imageFormat?.let { imageFormat = it.toAndroid() }
        this@toAndroid.addWatermark?.let { addWatermark = it }
    }

internal fun ImagenAspectRatio.toAndroid(): AndroidImagenAspectRatio = when (this) {
    ImagenAspectRatio.SQUARE_1x1 -> AndroidImagenAspectRatio.SQUARE_1x1
    ImagenAspectRatio.PORTRAIT_9x16 -> AndroidImagenAspectRatio.PORTRAIT_9x16
    ImagenAspectRatio.LANDSCAPE_16x9 -> AndroidImagenAspectRatio.LANDSCAPE_16x9
    ImagenAspectRatio.PORTRAIT_3x4 -> AndroidImagenAspectRatio.PORTRAIT_3x4
    ImagenAspectRatio.LANDSCAPE_4x3 -> AndroidImagenAspectRatio.LANDSCAPE_4x3
}

internal fun ImagenImageFormat.toAndroid(): AndroidImagenImageFormat = when (this) {
    is ImagenImageFormat.Png -> AndroidImagenImageFormat.png()
    is ImagenImageFormat.Jpeg -> AndroidImagenImageFormat.jpeg(compressionQuality)
}

internal fun ImagenSafetyFilterLevel.toAndroid(): AndroidImagenSafetyFilterLevel = when (this) {
    ImagenSafetyFilterLevel.BLOCK_LOW_AND_ABOVE -> AndroidImagenSafetyFilterLevel.BLOCK_LOW_AND_ABOVE
    ImagenSafetyFilterLevel.BLOCK_MEDIUM_AND_ABOVE -> AndroidImagenSafetyFilterLevel.BLOCK_MEDIUM_AND_ABOVE
    ImagenSafetyFilterLevel.BLOCK_ONLY_HIGH -> AndroidImagenSafetyFilterLevel.BLOCK_ONLY_HIGH
    ImagenSafetyFilterLevel.BLOCK_NONE -> AndroidImagenSafetyFilterLevel.BLOCK_NONE
}

internal fun ImagenPersonFilterLevel.toAndroid(): AndroidImagenPersonFilterLevel = when (this) {
    ImagenPersonFilterLevel.BLOCK_ALL -> AndroidImagenPersonFilterLevel.BLOCK_ALL
    ImagenPersonFilterLevel.ALLOW_ADULT -> AndroidImagenPersonFilterLevel.ALLOW_ADULT
    ImagenPersonFilterLevel.ALLOW_ALL -> AndroidImagenPersonFilterLevel.ALLOW_ALL
}

internal fun ImagenSafetySettings.toAndroid(): AndroidImagenSafetySettings =
    AndroidImagenSafetySettings(safetyFilterLevel.toAndroid(), personFilterLevel.toAndroid())

internal fun AndroidImagenGenerationResponse<AndroidImagenInlineImage>.toCommon(): ImagenGenerationResponse =
    ImagenGenerationResponse(
        images = images.map { it.toCommon() },
        filteredReason = filteredReason,
    )

internal fun AndroidImagenInlineImage.toCommon(): ImagenInlineImage =
    ImagenInlineImage(
        data = data,
        mimeType = mimeType,
    )
