package dev.ynagai.firebase.ai

import kotlinx.cinterop.ExperimentalForeignApi
import swiftPMImport.dev.ynagai.firebase.firebase.ai.KFBImagenGenerationResponse
import swiftPMImport.dev.ynagai.firebase.firebase.ai.KFBImagenModel

@OptIn(ExperimentalForeignApi::class)
actual class ImagenModel internal constructor(
    internal val apple: KFBImagenModel,
) {
    actual suspend fun generateImages(prompt: String): ImagenGenerationResponse {
        val result = awaitResult<KFBImagenGenerationResponse> { callback ->
            apple.generateImagesWithPrompt(prompt, completionHandler = callback)
        }
        return result.toCommon()
    }
}
