package dev.ynagai.firebase.auth

expect class EmailAuthProvider {
    companion object {
        fun getCredential(email: String, password: String): AuthCredential
    }
}

expect class GoogleAuthProvider {
    companion object {
        fun getCredential(idToken: String?, accessToken: String?): AuthCredential
    }
}

expect class PhoneAuthProvider {
    companion object {
        fun getCredential(verificationId: String, smsCode: String): AuthCredential
        suspend fun verifyPhoneNumber(auth: FirebaseAuth, phoneNumber: String): PhoneVerificationResult
    }
}

expect class OAuthProvider {
    companion object {
        fun getCredential(
            providerId: String,
            idToken: String,
            accessToken: String? = null,
        ): AuthCredential
    }
}
