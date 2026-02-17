package dev.ynagai.firebase.auth

import kotlinx.cinterop.ExperimentalForeignApi
import swiftPMImport.dev.ynagai.firebase.firebase.auth.FIREmailAuthProvider
import swiftPMImport.dev.ynagai.firebase.firebase.auth.FIRGoogleAuthProvider
import swiftPMImport.dev.ynagai.firebase.firebase.auth.FIRPhoneAuthProvider
import swiftPMImport.dev.ynagai.firebase.firebase.auth.FIROAuthProvider

@OptIn(ExperimentalForeignApi::class)
actual object EmailAuthProvider {
    actual fun credential(email: String, password: String): AuthCredential =
        AuthCredential(FIREmailAuthProvider.credentialWithEmail(email, password = password))
}

@OptIn(ExperimentalForeignApi::class)
actual object GoogleAuthProvider {
    actual fun credential(idToken: String?, accessToken: String?): AuthCredential =
        AuthCredential(
            FIRGoogleAuthProvider.credentialWithIDToken(
                idToken ?: "",
                accessToken = accessToken ?: "",
            )
        )
}

@OptIn(ExperimentalForeignApi::class)
actual object PhoneAuthProvider {
    actual fun credential(verificationId: String, smsCode: String): AuthCredential =
        AuthCredential(
            FIRPhoneAuthProvider.provider().credentialWithVerificationID(
                verificationId,
                verificationCode = smsCode,
            )
        )
}

@OptIn(ExperimentalForeignApi::class)
actual object OAuthProvider {
    actual fun credential(
        providerId: String,
        idToken: String?,
        accessToken: String?,
    ): AuthCredential =
        AuthCredential(
            FIROAuthProvider.credentialWithProviderID(
                providerId,
                IDToken = idToken ?: "",
                accessToken = accessToken ?: "",
            )
        )
}
