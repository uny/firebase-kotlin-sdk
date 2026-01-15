package dev.ynagai.firebase.ai

import kotlinx.cinterop.ExperimentalForeignApi
import swiftPMImport.firebase.kotlin.sdk.firebase.ai.KTFFirebaseAI
import dev.ynagai.firebase.Firebase
import dev.ynagai.firebase.FirebaseApp

@OptIn(ExperimentalForeignApi::class)
actual fun Firebase.ai(
    app: FirebaseApp,
    backend: GenerativeBackend
): FirebaseAI = FirebaseAI(
    KTFFirebaseAI.firebaseAIWithBackend(backend.apple)!!
)

@OptIn(ExperimentalForeignApi::class)
actual class FirebaseAI internal constructor(
    internal val apple: KTFFirebaseAI
)
