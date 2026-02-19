package dev.ynagai.firebase.auth

import kotlinx.cinterop.ExperimentalForeignApi
import swiftPMImport.dev.ynagai.firebase.firebase.auth.FIRMultiFactorAssertion

@OptIn(ExperimentalForeignApi::class)
actual class MultiFactorAssertion internal constructor(
    internal val apple: FIRMultiFactorAssertion,
) {
    actual val factorId: String
        get() = apple.factorID()
}
