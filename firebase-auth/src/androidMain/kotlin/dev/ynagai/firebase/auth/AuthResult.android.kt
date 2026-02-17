package dev.ynagai.firebase.auth

import com.google.firebase.auth.AuthResult as AndroidAuthResult

actual class AuthResult internal constructor(
    internal val android: AndroidAuthResult,
) {
    actual val user: FirebaseUser?
        get() = android.user?.let { FirebaseUser(it) }

    actual val additionalUserInfo: AdditionalUserInfo?
        get() = android.additionalUserInfo?.let {
            AdditionalUserInfo(
                providerId = it.providerId,
                username = it.username,
                profile = it.profile?.mapKeys { (key, _) -> key.toString() },
                isNewUser = it.isNewUser,
            )
        }

    actual val credential: AuthCredential?
        get() = android.credential?.let { AuthCredential(it) }
}
