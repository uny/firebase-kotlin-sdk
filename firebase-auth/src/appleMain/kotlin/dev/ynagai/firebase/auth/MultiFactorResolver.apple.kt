package dev.ynagai.firebase.auth

import kotlinx.cinterop.ExperimentalForeignApi
import swiftPMImport.dev.ynagai.firebase.firebase.auth.FIRMultiFactorInfo
import swiftPMImport.dev.ynagai.firebase.firebase.auth.FIRMultiFactorResolver

@OptIn(ExperimentalForeignApi::class)
actual class MultiFactorResolver internal constructor(
    internal val apple: FIRMultiFactorResolver,
) {
    actual val hints: List<MultiFactorInfo>
        get() = apple.hints().map { (it as FIRMultiFactorInfo).toCommon() }

    actual val session: MultiFactorSession
        get() = MultiFactorSession(apple.session())

    actual suspend fun resolveSignIn(assertion: MultiFactorAssertion): AuthResult =
        AuthResult(
            awaitResult { callback ->
                apple.resolveSignInWithAssertion(assertion.apple, completion = callback)
            }
        )
}
