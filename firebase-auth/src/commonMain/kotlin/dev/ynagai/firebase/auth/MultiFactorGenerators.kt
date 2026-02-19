package dev.ynagai.firebase.auth

expect class PhoneMultiFactorGenerator {
    companion object {
        fun getAssertion(credential: AuthCredential): MultiFactorAssertion
    }
}

expect class TotpMultiFactorGenerator {
    companion object {
        suspend fun generateSecret(session: MultiFactorSession): TotpSecret
        fun getAssertionForEnrollment(secret: TotpSecret, oneTimePassword: String): MultiFactorAssertion
        fun getAssertionForSignIn(enrollmentId: String, oneTimePassword: String): MultiFactorAssertion
    }
}
