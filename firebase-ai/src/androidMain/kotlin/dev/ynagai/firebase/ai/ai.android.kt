package dev.ynagai.firebase.ai

import com.google.firebase.Firebase as AndroidFirebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.FirebaseAI as AndroidFirebaseAI
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
)
