package dev.ynagai.firebase.auth

import kotlinx.cinterop.ExperimentalForeignApi
import swiftPMImport.dev.ynagai.firebase.firebase.auth.FIRPhoneAuthCredential
import swiftPMImport.dev.ynagai.firebase.firebase.auth.FIRPhoneMultiFactorGenerator
import swiftPMImport.dev.ynagai.firebase.firebase.auth.FIRTOTPMultiFactorGenerator
import swiftPMImport.dev.ynagai.firebase.firebase.auth.FIRTOTPSecret

@OptIn(ExperimentalForeignApi::class)
actual class PhoneMultiFactorGenerator {
    actual companion object {
        actual fun getAssertion(credential: AuthCredential): MultiFactorAssertion =
            MultiFactorAssertion(
                FIRPhoneMultiFactorGenerator.assertionWithCredential(credential.apple as FIRPhoneAuthCredential)
            )
    }
}

@OptIn(ExperimentalForeignApi::class)
actual class TotpMultiFactorGenerator {
    actual companion object {
        actual suspend fun generateSecret(session: MultiFactorSession): TotpSecret =
            TotpSecret(
                awaitResult { callback ->
                    FIRTOTPMultiFactorGenerator.generateSecretWithMultiFactorSession(
                        session.apple,
                        completion = callback,
                    )
                }
            )

        actual fun getAssertionForEnrollment(secret: TotpSecret, oneTimePassword: String): MultiFactorAssertion =
            MultiFactorAssertion(
                FIRTOTPMultiFactorGenerator.assertionForEnrollmentWithSecret(
                    secret.apple,
                    oneTimePassword = oneTimePassword,
                )
            )

        actual fun getAssertionForSignIn(enrollmentId: String, oneTimePassword: String): MultiFactorAssertion =
            MultiFactorAssertion(
                FIRTOTPMultiFactorGenerator.assertionForSignInWithEnrollmentID(
                    enrollmentId,
                    oneTimePassword = oneTimePassword,
                )
            )
    }
}
