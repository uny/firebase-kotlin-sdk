package dev.ynagai.firebase.auth

import com.google.firebase.auth.MultiFactorAssertion as AndroidMultiFactorAssertion

actual class MultiFactorAssertion internal constructor(
    internal val android: AndroidMultiFactorAssertion,
) {
    actual val factorId: String
        get() = android.factorId
}
