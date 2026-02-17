package dev.ynagai.firebase.auth

import com.google.firebase.auth.EmailAuthProvider as AndroidEmailAuthProvider
import com.google.firebase.auth.GoogleAuthProvider as AndroidGoogleAuthProvider
import com.google.firebase.auth.PhoneAuthProvider as AndroidPhoneAuthProvider
import com.google.firebase.auth.OAuthProvider as AndroidOAuthProvider

actual object EmailAuthProvider {
    actual fun credential(email: String, password: String): AuthCredential =
        AuthCredential(AndroidEmailAuthProvider.getCredential(email, password))
}

actual object GoogleAuthProvider {
    actual fun credential(idToken: String?, accessToken: String?): AuthCredential =
        AuthCredential(AndroidGoogleAuthProvider.getCredential(idToken, accessToken))
}

actual object PhoneAuthProvider {
    actual fun credential(verificationId: String, smsCode: String): AuthCredential =
        AuthCredential(AndroidPhoneAuthProvider.getCredential(verificationId, smsCode))
}

actual object OAuthProvider {
    actual fun credential(
        providerId: String,
        idToken: String?,
        accessToken: String?,
    ): AuthCredential {
        val builder = AndroidOAuthProvider.newCredentialBuilder(providerId)
        idToken?.let { builder.setIdToken(it) }
        accessToken?.let { builder.setAccessToken(it) }
        return AuthCredential(builder.build())
    }
}
