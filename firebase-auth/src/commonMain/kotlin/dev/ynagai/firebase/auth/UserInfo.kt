package dev.ynagai.firebase.auth

data class UserInfo(
    val uid: String,
    val providerId: String,
    val displayName: String?,
    val email: String?,
    val phoneNumber: String?,
    val photoUrl: String?,
)
