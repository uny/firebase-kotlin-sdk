package dev.ynagai.firebase.ai

import com.google.firebase.ai.type.GenerativeBackend as AndroidGenerativeBackend

actual class GenerativeBackend internal constructor(
    internal val android: AndroidGenerativeBackend
) {
    actual companion object {
        actual fun googleAI(): GenerativeBackend =
            GenerativeBackend(AndroidGenerativeBackend.googleAI())

        actual fun vertexAI(location: String): GenerativeBackend =
            GenerativeBackend(AndroidGenerativeBackend.vertexAI(location))
    }
}
