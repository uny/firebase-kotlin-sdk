package dev.ynagai.firebase.ai

import com.google.firebase.ai.ImagenModel as AndroidImagenModel

actual class ImagenModel internal constructor(
    internal val android: AndroidImagenModel
) {
    actual suspend fun generateImages(prompt: String): ImagenGenerationResponse =
        wrapAndroidException {
            android.generateImages(prompt).toCommon()
        }
}
