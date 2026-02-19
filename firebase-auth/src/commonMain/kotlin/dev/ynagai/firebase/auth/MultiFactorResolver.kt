package dev.ynagai.firebase.auth

expect class MultiFactorResolver {
    val hints: List<MultiFactorInfo>
    val session: MultiFactorSession
    suspend fun resolveSignIn(assertion: MultiFactorAssertion): AuthResult
}
