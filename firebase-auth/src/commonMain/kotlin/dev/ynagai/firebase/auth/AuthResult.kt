package dev.ynagai.firebase.auth

expect class AuthResult {
    val user: FirebaseUser?
    val additionalUserInfo: AdditionalUserInfo?
    val credential: AuthCredential?
}
