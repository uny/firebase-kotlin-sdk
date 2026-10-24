package dev.ynagai.firebase.ai

import dev.ynagai.firebase.Firebase
import dev.ynagai.firebase.FirebaseApp

// JVM has no official Firebase client SDK. This target is a fake intended for desktop previews
// and tests: models can be created, but every request fails with [FirebaseAIException]
// (errorType [FirebaseAIErrorType.INTERNAL]) so callers take their existing error paths.

internal const val JVM_UNAVAILABLE_MESSAGE =
    "Firebase AI Logic is not available on JVM (this target is a fake for desktop previews and tests)."

internal fun unavailableOnJvm(): Nothing =
    throw FirebaseAIException(JVM_UNAVAILABLE_MESSAGE, errorType = FirebaseAIErrorType.INTERNAL)

@Suppress("UNUSED_PARAMETER")
actual fun Firebase.ai(
    app: FirebaseApp,
    backend: GenerativeBackend,
    useLimitedUseAppCheckTokens: Boolean,
): FirebaseAI = FirebaseAI()

@Suppress("UNUSED_PARAMETER")
actual class FirebaseAI internal constructor() {
    actual fun generativeModel(
        modelName: String,
        generationConfig: GenerationConfig?,
        safetySettings: List<SafetySetting>?,
        systemInstruction: Content?,
        tools: List<Tool>?,
        toolConfig: ToolConfig?,
    ): GenerativeModel = GenerativeModel()

    @Suppress("DEPRECATION")
    actual fun imagenModel(
        modelName: String,
        generationConfig: ImagenGenerationConfig?,
        safetySettings: ImagenSafetySettings?,
    ): ImagenModel = ImagenModel()

    actual fun liveModel(
        modelName: String,
        liveGenerationConfig: LiveGenerationConfig?,
        systemInstruction: Content?,
        tools: List<Tool>?,
    ): LiveGenerativeModel = LiveGenerativeModel()
}
