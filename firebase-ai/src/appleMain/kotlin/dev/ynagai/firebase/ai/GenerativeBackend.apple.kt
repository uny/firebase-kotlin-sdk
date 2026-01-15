package dev.ynagai.firebase.ai

import kotlinx.cinterop.ExperimentalForeignApi
import swiftPMImport.firebase.kotlin.sdk.firebase.ai.KTFBackend

@OptIn(ExperimentalForeignApi::class)
actual class GenerativeBackend internal constructor(
    internal val apple: KTFBackend
) {
    actual companion object {
        actual fun googleAI(): GenerativeBackend =
            GenerativeBackend(KTFBackend.googleAI()!!)
    }
}
