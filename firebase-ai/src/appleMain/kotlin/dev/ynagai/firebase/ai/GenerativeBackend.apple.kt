package dev.ynagai.firebase.ai

import FirebaseAIBridge.Backend

actual class GenerativeBackend internal constructor(
    internal val apple: Backend
) {
    actual companion object {
        actual fun googleAI(): GenerativeBackend =
            GenerativeBackend(Backend.googleAI())
    }
}
