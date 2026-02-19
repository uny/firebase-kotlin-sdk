package dev.ynagai.firebase.auth

import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneMultiFactorGenerator as AndroidPhoneMultiFactorGenerator
import com.google.firebase.auth.TotpMultiFactorGenerator as AndroidTotpMultiFactorGenerator
import kotlinx.coroutines.tasks.await

actual class PhoneMultiFactorGenerator {
    actual companion object {
        actual fun getAssertion(credential: AuthCredential): MultiFactorAssertion =
            MultiFactorAssertion(
                AndroidPhoneMultiFactorGenerator.getAssertion(credential.android as PhoneAuthCredential)
            )
    }
}

actual class TotpMultiFactorGenerator {
    actual companion object {
        actual suspend fun generateSecret(session: MultiFactorSession): TotpSecret =
            TotpSecret(AndroidTotpMultiFactorGenerator.generateSecret(session.android).await())

        actual fun getAssertionForEnrollment(secret: TotpSecret, oneTimePassword: String): MultiFactorAssertion =
            MultiFactorAssertion(
                AndroidTotpMultiFactorGenerator.getAssertionForEnrollment(secret.android, oneTimePassword)
            )

        actual fun getAssertionForSignIn(enrollmentId: String, oneTimePassword: String): MultiFactorAssertion =
            MultiFactorAssertion(
                AndroidTotpMultiFactorGenerator.getAssertionForSignIn(enrollmentId, oneTimePassword)
            )
    }
}
