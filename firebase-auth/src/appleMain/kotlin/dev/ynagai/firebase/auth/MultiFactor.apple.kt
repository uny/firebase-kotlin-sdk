package dev.ynagai.firebase.auth

import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDate
import platform.Foundation.timeIntervalSince1970
import swiftPMImport.dev.ynagai.firebase.firebase.auth.FIRMultiFactor
import swiftPMImport.dev.ynagai.firebase.firebase.auth.FIRMultiFactorInfo
import swiftPMImport.dev.ynagai.firebase.firebase.auth.FIRPhoneMultiFactorInfo

@OptIn(ExperimentalForeignApi::class)
actual class MultiFactor internal constructor(
    internal val apple: FIRMultiFactor,
) {
    actual val enrolledFactors: List<MultiFactorInfo>
        get() = apple.enrolledFactors().map { (it as FIRMultiFactorInfo).toCommon() }

    actual suspend fun getSession(): MultiFactorSession =
        MultiFactorSession(
            awaitResult { callback ->
                apple.getSessionWithCompletion(callback)
            }
        )

    actual suspend fun enroll(assertion: MultiFactorAssertion, displayName: String?) {
        await { callback ->
            apple.enrollWithAssertion(assertion.apple, displayName = displayName, completion = callback)
        }
    }

    actual suspend fun unenroll(multiFactorInfo: MultiFactorInfo) {
        unenroll(multiFactorInfo.uid)
    }

    actual suspend fun unenroll(factorUid: String) {
        await { callback ->
            apple.unenrollWithFactorUID(factorUid, completion = callback)
        }
    }
}

@OptIn(ExperimentalForeignApi::class)
internal fun FIRMultiFactorInfo.toCommon(): MultiFactorInfo {
    if (this is FIRPhoneMultiFactorInfo) {
        return PhoneMultiFactorInfo(
            uid = UID(),
            displayName = displayName(),
            factorId = factorID(),
            enrollmentTimestamp = enrollmentDate().toMillis(),
            phoneNumber = phoneNumber(),
        )
    }
    return TotpMultiFactorInfo(
        uid = UID(),
        displayName = displayName(),
        factorId = factorID(),
        enrollmentTimestamp = enrollmentDate().toMillis(),
    )
}

internal fun NSDate.toMillis(): Long = (timeIntervalSince1970 * 1000).toLong()
