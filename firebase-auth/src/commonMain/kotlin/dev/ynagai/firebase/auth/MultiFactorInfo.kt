package dev.ynagai.firebase.auth

sealed class MultiFactorInfo {
    abstract val uid: String
    abstract val displayName: String?
    abstract val factorId: String
    abstract val enrollmentTimestamp: Long
}

data class PhoneMultiFactorInfo(
    override val uid: String,
    override val displayName: String?,
    override val factorId: String,
    override val enrollmentTimestamp: Long,
    val phoneNumber: String,
) : MultiFactorInfo()

data class TotpMultiFactorInfo(
    override val uid: String,
    override val displayName: String?,
    override val factorId: String,
    override val enrollmentTimestamp: Long,
) : MultiFactorInfo()
