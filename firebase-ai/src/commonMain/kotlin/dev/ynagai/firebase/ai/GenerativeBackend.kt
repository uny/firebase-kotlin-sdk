package dev.ynagai.firebase.ai

expect class GenerativeBackend {
    companion object {
        fun googleAI(): GenerativeBackend
        fun vertexAI(location: String = "us-central1"): GenerativeBackend
    }
}
