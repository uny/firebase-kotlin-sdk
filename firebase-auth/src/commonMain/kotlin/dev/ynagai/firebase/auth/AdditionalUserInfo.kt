package dev.ynagai.firebase.auth

data class AdditionalUserInfo(
    val providerId: String?,
    val username: String?,
    val profile: Map<String, Any?>?,
    val isNewUser: Boolean,
)
