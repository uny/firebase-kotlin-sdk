package dev.ynagai.firebase.auth

import kotlinx.cinterop.ExperimentalForeignApi
import swiftPMImport.dev.ynagai.firebase.firebase.auth.FIRAuthCredential
import swiftPMImport.dev.ynagai.firebase.firebase.auth.FIRAuthDataResult

@OptIn(ExperimentalForeignApi::class)
actual class AuthResult internal constructor(
    internal val apple: FIRAuthDataResult,
) {
    actual val user: FirebaseUser?
        get() = apple.user()?.let { FirebaseUser(it) }

    actual val additionalUserInfo: AdditionalUserInfo?
        get() = apple.additionalUserInfo()?.let {
            AdditionalUserInfo(
                providerId = it.providerID(),
                username = it.username(),
                profile = it.profile()?.mapKeys { (key, _) -> key.toString() },
                isNewUser = it.isNewUser(),
            )
        }

    actual val credential: AuthCredential?
        get() = (apple.credential() as? FIRAuthCredential)?.let { AuthCredential(it) }
}
