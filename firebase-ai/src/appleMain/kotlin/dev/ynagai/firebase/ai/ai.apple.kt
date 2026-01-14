package dev.ynagai.firebase.ai

import FirebaseAIBridge.FirebaseAI as AppleFirebaseAI
import dev.ynagai.firebase.Firebase
import dev.ynagai.firebase.FirebaseApp

@Suppress("UNUSED_PARAMETER")
actual fun Firebase.ai(
    app: FirebaseApp,
    backend: GenerativeBackend
): FirebaseAI = FirebaseAI(
    AppleFirebaseAI.ai(backend.apple)
)

actual class FirebaseAI internal constructor(
    internal val apple: AppleFirebaseAI
)
