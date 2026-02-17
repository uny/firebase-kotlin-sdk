package dev.ynagai.firebase.auth

import com.google.firebase.auth.EmailAuthProvider as AndroidEmailAuthProvider
import com.google.firebase.auth.GoogleAuthProvider as AndroidGoogleAuthProvider
import com.google.firebase.auth.PhoneAuthProvider as AndroidPhoneAuthProvider
import com.google.firebase.auth.OAuthProvider as AndroidOAuthProvider

actual class EmailAuthProvider {
    actual companion object {
        actual fun getCredential(email: String, password: String): AuthCredential =
            AuthCredential(AndroidEmailAuthProvider.getCredential(email, password))
    }
}

actual class GoogleAuthProvider {
    actual companion object {
        actual fun getCredential(idToken: String?, accessToken: String?): AuthCredential =
            AuthCredential(AndroidGoogleAuthProvider.getCredential(idToken, accessToken))
    }
}

actual class PhoneAuthProvider {
    actual companion object {
        actual fun getCredential(verificationId: String, smsCode: String): AuthCredential =
            AuthCredential(AndroidPhoneAuthProvider.getCredential(verificationId, smsCode))
    }
}

actual class OAuthProvider {
    actual companion object {
        actual fun getCredential(
            providerId: String,
            idToken: String,
            accessToken: String?,
        ): AuthCredential {
            val builder = AndroidOAuthProvider.newCredentialBuilder(providerId)
            builder.setIdToken(idToken)
            accessToken?.let { builder.setAccessToken(it) }
            return AuthCredential(builder.build())
        }
    }
}
