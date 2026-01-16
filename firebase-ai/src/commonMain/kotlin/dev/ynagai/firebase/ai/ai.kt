package dev.ynagai.firebase.ai

import dev.ynagai.firebase.Firebase
import dev.ynagai.firebase.FirebaseApp
import dev.ynagai.firebase.app

expect fun Firebase.ai(
    app: FirebaseApp = Firebase.app,
    backend: GenerativeBackend = GenerativeBackend.googleAI(),
    useLimitedUseAppCheckTokens: Boolean = false,
): FirebaseAI

expect class FirebaseAI {
    fun generativeModel(
        modelName: String,
        generationConfig: GenerationConfig? = null,
        safetySettings: List<SafetySetting>? = null,
        systemInstruction: Content? = null,
    ): GenerativeModel
}
