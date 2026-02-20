package dev.ynagai.firebase.auth

import com.google.firebase.auth.MultiFactorResolver as AndroidMultiFactorResolver
import kotlinx.coroutines.tasks.await

actual class MultiFactorResolver internal constructor(
    internal val android: AndroidMultiFactorResolver,
) {
    actual val hints: List<MultiFactorInfo>
        get() = android.hints.map { it.toCommon() }

    actual val session: MultiFactorSession
        get() = MultiFactorSession(android.session)

    actual suspend fun resolveSignIn(assertion: MultiFactorAssertion): AuthResult =
        AuthResult(android.resolveSignIn(assertion.android).await())
}
