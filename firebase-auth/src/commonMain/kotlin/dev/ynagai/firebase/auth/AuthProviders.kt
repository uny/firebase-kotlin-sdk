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
        suspend fun verifyPhoneNumber(auth: FirebaseAuth, phoneNumber: String, session: MultiFactorSession): PhoneVerificationResult
    }
}

expect class OAuthProvider {
    companion object {
        fun getCredential(
            providerId: String,
            idToken: String,
            accessToken: String? = null,
        ): AuthCredential

        /**
         * Returns an [AuthCredential] for the specified [providerId] using an ID token that was
         * issued against a hashed nonce (e.g. Sign in with Apple). Firebase verifies the SHA-256
         * hash of [rawNonce] matches the `nonce` claim in [idToken]; omitting the raw nonce causes
         * sign-in to fail with `Nonce is missing in the request.`
         */
        fun getCredential(
            providerId: String,
            idToken: String,
            rawNonce: String,
            accessToken: String? = null,
        ): AuthCredential
    }
}

expect class FacebookAuthProvider {
    companion object {
        fun getCredential(accessToken: String): AuthCredential
    }
}

expect class GithubAuthProvider {
    companion object {
        fun getCredential(token: String): AuthCredential
    }
}

expect class TwitterAuthProvider {
    companion object {
        fun getCredential(token: String, secret: String): AuthCredential
    }
}
