package dev.ynagai.firebase.ai

import kotlinx.cinterop.ExperimentalForeignApi
import swiftPMImport.firebase.kotlin.sdk.firebase.ai.KFBBackend

@OptIn(ExperimentalForeignApi::class)
actual class GenerativeBackend internal constructor(
    internal val apple: KFBBackend
) {
    actual companion object {
        actual fun googleAI(): GenerativeBackend =
            GenerativeBackend(KFBBackend.googleAI())

        actual fun vertexAI(location: String): GenerativeBackend =
            GenerativeBackend(KFBBackend.vertexAIWithLocation(location))
    }
}
