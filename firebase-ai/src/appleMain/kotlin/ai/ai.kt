package dev.ynagai.firebase.ai

import dev.ynagai.firebase.Firebase

actual val Firebase.ai: FirebaseAI
    get() = TODO("Not yet implemented")

actual class FirebaseAI(internal val ios: FirebaseAIBridge.FirebaseAI) {
}
