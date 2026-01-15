package dev.ynagai.firebase.ai

import kotlinx.cinterop.ExperimentalForeignApi
import dev.ynagai.firebase.ai.cinterop.FIRFirebaseAI
import dev.ynagai.firebase.Firebase
import dev.ynagai.firebase.FirebaseApp

@OptIn(ExperimentalForeignApi::class)
@Suppress("UNUSED_PARAMETER")
actual fun Firebase.ai(
    app: FirebaseApp,
    backend: GenerativeBackend
): FirebaseAI = FirebaseAI(
    FIRFirebaseAI.ai(backend = backend.apple)
)

@OptIn(ExperimentalForeignApi::class)
actual class FirebaseAI internal constructor(
    internal val apple: FIRFirebaseAI
)
