package dev.ynagai.firebase.auth

data class ActionCodeSettings(
    val url: String,
    val handleCodeInApp: Boolean = false,
    val androidPackageName: String? = null,
    val androidInstallIfNotAvailable: Boolean = false,
    val androidMinimumVersion: String? = null,
    val iOSBundleId: String? = null,
    val dynamicLinkDomain: String? = null,
)
