package dev.ynagai.firebase.ai

import kotlinx.cinterop.ExperimentalForeignApi
import swiftPMImport.firebase.kotlin.sdk.firebase.ai.KFBFirebaseAI
import dev.ynagai.firebase.Firebase
import dev.ynagai.firebase.FirebaseApp

@OptIn(ExperimentalForeignApi::class)
actual fun Firebase.ai(
    app: FirebaseApp,
    backend: GenerativeBackend,
    useLimitedUseAppCheckTokens: Boolean,
): FirebaseAI = FirebaseAI(
    KFBFirebaseAI.firebaseAIWithApp(
        app = app.apple,
        backend = backend.apple,
        useLimitedUseAppCheckTokens = useLimitedUseAppCheckTokens,
    ),
)

@OptIn(ExperimentalForeignApi::class)
actual class FirebaseAI internal constructor(
    internal val apple: KFBFirebaseAI,
) {
    actual fun generativeModel(
        modelName: String,
        generationConfig: GenerationConfig?,
        safetySettings: List<SafetySetting>?,
        systemInstruction: Content?,
    ): GenerativeModel {
        TODO("iOS implementation pending")
    }
}
