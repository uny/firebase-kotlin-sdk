package dev.ynagai.firebase.auth

import com.google.firebase.auth.AuthCredential as AndroidAuthCredential

actual class AuthCredential internal constructor(
    internal val android: AndroidAuthCredential,
) {
    actual val providerId: String
        get() = android.provider
}
