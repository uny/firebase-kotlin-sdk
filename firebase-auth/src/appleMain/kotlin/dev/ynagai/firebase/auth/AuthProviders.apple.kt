package dev.ynagai.firebase.auth

import kotlinx.cinterop.ExperimentalForeignApi
import swiftPMImport.dev.ynagai.firebase.firebase.auth.FIREmailAuthProvider
import swiftPMImport.dev.ynagai.firebase.firebase.auth.FIRGoogleAuthProvider
import swiftPMImport.dev.ynagai.firebase.firebase.auth.FIRPhoneAuthProvider
import swiftPMImport.dev.ynagai.firebase.firebase.auth.FIROAuthProvider

@OptIn(ExperimentalForeignApi::class)
actual class EmailAuthProvider {
    actual companion object {
        actual fun getCredential(email: String, password: String): AuthCredential =
            AuthCredential(FIREmailAuthProvider.credentialWithEmail(email, password = password))
    }
}

@OptIn(ExperimentalForeignApi::class)
actual class GoogleAuthProvider {
    actual companion object {
        actual fun getCredential(idToken: String?, accessToken: String?): AuthCredential =
            AuthCredential(
                FIRGoogleAuthProvider.credentialWithIDToken(
                    idToken ?: throw IllegalArgumentException("idToken is required on iOS"),
                    accessToken = accessToken ?: throw IllegalArgumentException("accessToken is required on iOS"),
                )
            )
    }
}

@OptIn(ExperimentalForeignApi::class)
actual class PhoneAuthProvider {
    actual companion object {
        actual fun getCredential(verificationId: String, smsCode: String): AuthCredential =
            AuthCredential(
                FIRPhoneAuthProvider.provider().credentialWithVerificationID(
                    verificationId,
                    verificationCode = smsCode,
                )
            )
    }
}

@OptIn(ExperimentalForeignApi::class)
actual class OAuthProvider {
    actual companion object {
        actual fun getCredential(
            providerId: String,
            idToken: String,
            accessToken: String?,
        ): AuthCredential =
            AuthCredential(
                FIROAuthProvider.credentialWithProviderID(
                    providerId,
                    IDToken = idToken,
                    accessToken = accessToken,
                )
            )
    }
}
