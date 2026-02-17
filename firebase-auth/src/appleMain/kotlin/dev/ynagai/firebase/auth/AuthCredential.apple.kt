package dev.ynagai.firebase.auth

import kotlinx.cinterop.ExperimentalForeignApi
import swiftPMImport.dev.ynagai.firebase.firebase.auth.FIRAuthCredential

@OptIn(ExperimentalForeignApi::class)
actual class AuthCredential internal constructor(
    internal val apple: FIRAuthCredential,
) {
    actual val providerId: String
        get() = apple.provider()
}
