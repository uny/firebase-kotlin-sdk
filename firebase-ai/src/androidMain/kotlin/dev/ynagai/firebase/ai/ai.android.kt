package dev.ynagai.firebase.ai

import com.google.firebase.Firebase as AndroidFirebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.FirebaseAI as AndroidFirebaseAI
import com.google.firebase.ai.type.PublicPreviewAPI
import dev.ynagai.firebase.Firebase
import dev.ynagai.firebase.FirebaseApp

actual fun Firebase.ai(
    app: FirebaseApp,
    backend: GenerativeBackend,
    useLimitedUseAppCheckTokens: Boolean,
): FirebaseAI = FirebaseAI(
    AndroidFirebase.ai(
        app = app.android,
        backend = backend.android,
        useLimitedUseAppCheckTokens = useLimitedUseAppCheckTokens,
    ),
)

actual class FirebaseAI internal constructor(
    internal val android: AndroidFirebaseAI
) {
    actual fun generativeModel(
        modelName: String,
        generationConfig: GenerationConfig?,
        safetySettings: List<SafetySetting>?,
        systemInstruction: Content?,
        tools: List<Tool>?,
        toolConfig: ToolConfig?,
    ): GenerativeModel = GenerativeModel(
        android.generativeModel(
            modelName = modelName,
            generationConfig = generationConfig?.toAndroid(),
            safetySettings = safetySettings?.map { it.toAndroid() },
            systemInstruction = systemInstruction?.toAndroid(),
            tools = tools?.map { it.toAndroid() },
            toolConfig = toolConfig?.toAndroid(),
        )
    )

    actual fun imagenModel(
        modelName: String,
        generationConfig: ImagenGenerationConfig?,
        safetySettings: ImagenSafetySettings?,
    ): ImagenModel = ImagenModel(
        android.imagenModel(
            modelName = modelName,
            generationConfig = generationConfig?.toAndroid(),
            safetySettings = safetySettings?.toAndroid(),
        )
    )

    @OptIn(PublicPreviewAPI::class)
    actual fun liveModel(
        modelName: String,
        liveGenerationConfig: LiveGenerationConfig?,
        systemInstruction: Content?,
        tools: List<Tool>?,
    ): LiveGenerativeModel = LiveGenerativeModel(
        android.liveModel(
            modelName = modelName,
            generationConfig = liveGenerationConfig?.toAndroid(),
            tools = tools?.map { it.toAndroid() },
            systemInstruction = systemInstruction?.toAndroid(),
        )
    )
}
