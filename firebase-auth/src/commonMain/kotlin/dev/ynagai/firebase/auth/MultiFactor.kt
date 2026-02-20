package dev.ynagai.firebase.auth

expect class MultiFactor {
    val enrolledFactors: List<MultiFactorInfo>
    suspend fun getSession(): MultiFactorSession
    suspend fun enroll(assertion: MultiFactorAssertion, displayName: String?)
    suspend fun unenroll(multiFactorInfo: MultiFactorInfo)
    suspend fun unenroll(factorUid: String)
}
