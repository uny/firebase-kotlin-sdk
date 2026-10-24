package dev.ynagai.firebase.ai

@Suppress("UNUSED_PARAMETER")
actual class GenerativeBackend private constructor() {
    actual companion object {
        actual fun googleAI(): GenerativeBackend = GenerativeBackend()

        actual fun vertexAI(location: String): GenerativeBackend = GenerativeBackend()
    }
}
