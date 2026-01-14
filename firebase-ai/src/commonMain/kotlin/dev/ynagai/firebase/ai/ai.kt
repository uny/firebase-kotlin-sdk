package dev.ynagai.firebase.ai

import dev.ynagai.firebase.Firebase
import dev.ynagai.firebase.FirebaseApp
import dev.ynagai.firebase.app

expect fun Firebase.ai(
    app: FirebaseApp = Firebase.app,
    backend: GenerativeBackend = GenerativeBackend.googleAI()
): FirebaseAI

expect class FirebaseAI
