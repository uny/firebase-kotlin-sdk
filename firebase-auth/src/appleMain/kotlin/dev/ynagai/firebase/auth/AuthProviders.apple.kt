package dev.ynagai.firebase.auth

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.suspendCancellableCoroutine
import swiftPMImport.dev.ynagai.firebase.firebase.auth.FIREmailAuthProvider
import swiftPMImport.dev.ynagai.firebase.firebase.auth.FIRGoogleAuthProvider
import swiftPMImport.dev.ynagai.firebase.firebase.auth.FIRPhoneAuthProvider
import swiftPMImport.dev.ynagai.firebase.firebase.auth.FIRFacebookAuthProvider
import swiftPMImport.dev.ynagai.firebase.firebase.auth.FIRGitHubAuthProvider
import swiftPMImport.dev.ynagai.firebase.firebase.auth.FIROAuthProvider
import swiftPMImport.dev.ynagai.firebase.firebase.auth.FIRTwitterAuthProvider
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

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

        actual suspend fun verifyPhoneNumber(
            auth: FirebaseAuth,
            phoneNumber: String,
        ): PhoneVerificationResult = suspendCancellableCoroutine { continuation ->
            FIRPhoneAuthProvider.providerWithAuth(auth.apple)
                .verifyPhoneNumber(phoneNumber, UIDelegate = null) { verificationId, error ->
                    when {
                        error != null -> {
                            if (continuation.isActive) {
                                continuation.resumeWithException(error.toAuthException())
                            }
                        }
                        verificationId != null -> {
                            if (continuation.isActive) {
                                continuation.resume(PhoneVerificationResult.CodeSent(verificationId))
                            }
                        }
                        else -> {
                            if (continuation.isActive) {
                                continuation.resumeWithException(
                                    FirebaseAuthException(
                                        "Phone verification completed without verificationId or error.",
                                        FirebaseAuthExceptionCode.UNKNOWN,
                                    )
                                )
                            }
                        }
                    }
                }
        }

        actual suspend fun verifyPhoneNumber(
            auth: FirebaseAuth,
            phoneNumber: String,
            session: MultiFactorSession,
        ): PhoneVerificationResult = suspendCancellableCoroutine { continuation ->
            FIRPhoneAuthProvider.providerWithAuth(auth.apple)
                .verifyPhoneNumber(phoneNumber, UIDelegate = null, multiFactorSession = session.apple) { verificationId, error ->
                    when {
                        error != null -> {
                            if (continuation.isActive) {
                                continuation.resumeWithException(error.toAuthException())
                            }
                        }
                        verificationId != null -> {
                            if (continuation.isActive) {
                                continuation.resume(PhoneVerificationResult.CodeSent(verificationId))
                            }
                        }
                        else -> {
                            if (continuation.isActive) {
                                continuation.resumeWithException(
                                    FirebaseAuthException(
                                        "Phone verification completed without verificationId or error.",
                                        FirebaseAuthExceptionCode.UNKNOWN,
                                    )
                                )
                            }
                        }
                    }
                }
        }
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

@OptIn(ExperimentalForeignApi::class)
actual class FacebookAuthProvider {
    actual companion object {
        actual fun getCredential(accessToken: String): AuthCredential =
            AuthCredential(FIRFacebookAuthProvider.credentialWithAccessToken(accessToken))
    }
}

@OptIn(ExperimentalForeignApi::class)
actual class GithubAuthProvider {
    actual companion object {
        actual fun getCredential(token: String): AuthCredential =
            AuthCredential(FIRGitHubAuthProvider.credentialWithToken(token))
    }
}

@OptIn(ExperimentalForeignApi::class)
actual class TwitterAuthProvider {
    actual companion object {
        actual fun getCredential(token: String, secret: String): AuthCredential =
            AuthCredential(FIRTwitterAuthProvider.credentialWithToken(token, secret = secret))
    }
}
