package dev.ynagai.firebase.ai

import kotlinx.cinterop.ExperimentalForeignApi
import swiftPMImport.firebase.kotlin.sdk.firebase.ai.KFBFirebaseAI
import dev.ynagai.firebase.Firebase
import dev.ynagai.firebase.FirebaseApp

@OptIn(ExperimentalForeignApi::class)
actual fun Firebase.ai(
    app: FirebaseApp,
    backend: GenerativeBackend,
): FirebaseAI = FirebaseAI(
    KFBFirebaseAI.firebaseAIWithApp(
        app = app.apple,
        backend = backend.apple,
        useLimitedUseAppCheckTokens = false
    )
)

@OptIn(ExperimentalForeignApi::class)
actual class FirebaseAI internal constructor(
    internal val apple: KFBFirebaseAI
)
