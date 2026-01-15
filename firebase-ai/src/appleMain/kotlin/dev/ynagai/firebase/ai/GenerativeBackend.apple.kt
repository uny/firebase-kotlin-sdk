package dev.ynagai.firebase.ai

import kotlinx.cinterop.ExperimentalForeignApi
import dev.ynagai.firebase.ai.cinterop.FIRBackend

@OptIn(ExperimentalForeignApi::class)
actual class GenerativeBackend internal constructor(
    internal val apple: FIRBackend
) {
    actual companion object {
        actual fun googleAI(): GenerativeBackend =
            GenerativeBackend(FIRBackend.googleAI())
    }
}
