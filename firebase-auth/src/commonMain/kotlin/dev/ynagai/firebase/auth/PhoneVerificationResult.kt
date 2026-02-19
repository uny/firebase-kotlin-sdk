package dev.ynagai.firebase.auth

sealed class PhoneVerificationResult {
    data class CodeSent(val verificationId: String) : PhoneVerificationResult()
    data class AutoVerified(val credential: AuthCredential) : PhoneVerificationResult()
}
