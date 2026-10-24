package dev.ynagai.firebase.ai

@Suppress("DEPRECATION", "UNUSED_PARAMETER")
actual class ImagenModel internal constructor() {
    actual suspend fun generateImages(prompt: String): ImagenGenerationResponse =
        unavailableOnJvm()
}
