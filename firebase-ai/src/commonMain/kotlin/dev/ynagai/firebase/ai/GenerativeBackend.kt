package dev.ynagai.firebase.ai

expect class GenerativeBackend {
    companion object {
        fun googleAI(): GenerativeBackend
    }
}
