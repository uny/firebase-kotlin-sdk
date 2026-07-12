package dev.ynagai.firebase.ai

import dev.ynagai.firebase.Firebase
import dev.ynagai.firebase.FirebaseApp
import kotlinx.cinterop.ExperimentalForeignApi
import swiftPMImport.dev.ynagai.firebase.firebase.ai.KFBFirebaseAI

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
    // TODO(hybrid-inference): wire onDeviceConfig through once `uny/firebase-objc-sdk` publishes
    //  a Hybrid Inference / on-device wrapper (tracked alongside spike/hybrid-inference there).
    //  Until then, a non-null onDeviceConfig is accepted for source compatibility but has no
    //  effect and silently falls back to in-cloud inference, matching upstream's IN_CLOUD default.
    actual fun generativeModel(
        modelName: String,
        generationConfig: GenerationConfig?,
        safetySettings: List<SafetySetting>?,
        systemInstruction: Content?,
        tools: List<Tool>?,
        toolConfig: ToolConfig?,
        onDeviceConfig: OnDeviceConfig?,
    ): GenerativeModel = GenerativeModel(
        apple.generativeModelWithModelName(
            modelName = modelName,
            generationConfig = generationConfig?.toApple(),
            safetySettings = safetySettings?.map { it.toApple() },
            tools = tools?.map { it.toApple() },
            toolConfig = toolConfig?.toApple(),
            systemInstruction = systemInstruction?.toApple(),
            requestOptions = null,
        ),
    )

    @Suppress("DEPRECATION")
    actual fun imagenModel(
        modelName: String,
        generationConfig: ImagenGenerationConfig?,
        safetySettings: ImagenSafetySettings?,
    ): ImagenModel = ImagenModel(
        apple.imagenModelWithModelName(
            modelName = modelName,
            generationConfig = generationConfig?.toApple(),
            safetySettings = safetySettings?.toApple(),
            requestOptions = null,
        ),
    )

    actual fun liveModel(
        modelName: String,
        liveGenerationConfig: LiveGenerationConfig?,
        systemInstruction: Content?,
        tools: List<Tool>?,
    ): LiveGenerativeModel = LiveGenerativeModel(
        apple.liveModelWithModelName(
            modelName = modelName,
            generationConfig = liveGenerationConfig?.toApple(),
            tools = tools?.map { it.toApple() },
            toolConfig = null,
            systemInstruction = systemInstruction?.toApple(),
            requestOptions = null,
        ),
    )
}
