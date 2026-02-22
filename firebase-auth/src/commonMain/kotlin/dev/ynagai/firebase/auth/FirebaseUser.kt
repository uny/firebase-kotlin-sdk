package dev.ynagai.firebase.auth

expect class FirebaseUser {
    val uid: String
    val email: String?
    val displayName: String?
    val photoUrl: String?
    val phoneNumber: String?
    val isAnonymous: Boolean
    val isEmailVerified: Boolean
    val providerId: String
    val providerData: List<UserInfo>
    val metadata: UserMetadata?
    val multiFactor: MultiFactor

    suspend fun delete()
    suspend fun reload()
    suspend fun getIdToken(forceRefresh: Boolean): String?
    suspend fun updatePassword(password: String)
    suspend fun updateProfile(displayName: String? = null, photoUrl: String? = null)
    suspend fun sendEmailVerification()
    suspend fun sendEmailVerification(actionCodeSettings: ActionCodeSettings)
    suspend fun linkWithCredential(credential: AuthCredential): AuthResult
    suspend fun unlink(providerId: String): FirebaseUser
    suspend fun reauthenticate(credential: AuthCredential)
    suspend fun updatePhoneNumber(credential: AuthCredential)
    suspend fun verifyBeforeUpdateEmail(newEmail: String)
}
