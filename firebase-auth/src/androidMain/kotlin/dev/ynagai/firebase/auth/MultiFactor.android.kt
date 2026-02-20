package dev.ynagai.firebase.auth

import com.google.firebase.auth.MultiFactor as AndroidMultiFactor
import com.google.firebase.auth.MultiFactorInfo as AndroidMultiFactorInfo
import com.google.firebase.auth.PhoneMultiFactorInfo as AndroidPhoneMultiFactorInfo
import com.google.firebase.auth.TotpMultiFactorInfo as AndroidTotpMultiFactorInfo
import kotlinx.coroutines.tasks.await

actual class MultiFactor internal constructor(
    internal val android: AndroidMultiFactor,
) {
    actual val enrolledFactors: List<MultiFactorInfo>
        get() = android.enrolledFactors.map { it.toCommon() }

    actual suspend fun getSession(): MultiFactorSession =
        MultiFactorSession(android.session.await())

    actual suspend fun enroll(assertion: MultiFactorAssertion, displayName: String?) {
        android.enroll(assertion.android, displayName).await()
    }

    actual suspend fun unenroll(multiFactorInfo: MultiFactorInfo) {
        android.unenroll(multiFactorInfo.uid).await()
    }

    actual suspend fun unenroll(factorUid: String) {
        android.unenroll(factorUid).await()
    }
}

internal fun AndroidMultiFactorInfo.toCommon(): MultiFactorInfo = when (this) {
    is AndroidPhoneMultiFactorInfo -> PhoneMultiFactorInfo(
        uid = uid,
        displayName = displayName,
        factorId = factorId,
        enrollmentTimestamp = enrollmentTimestamp,
        phoneNumber = phoneNumber,
    )
    is AndroidTotpMultiFactorInfo -> TotpMultiFactorInfo(
        uid = uid,
        displayName = displayName,
        factorId = factorId,
        enrollmentTimestamp = enrollmentTimestamp,
    )
    else -> TotpMultiFactorInfo(
        uid = uid,
        displayName = displayName,
        factorId = factorId,
        enrollmentTimestamp = enrollmentTimestamp,
    )
}
