package dev.ynagai.firebase.auth

data class ActionCodeSettings(
    val url: String,
    val handleCodeInApp: Boolean = false,
    val androidPackageName: String? = null,
    val androidInstallIfNotAvailable: Boolean = false,
    val androidMinimumVersion: String? = null,
    val iOSBundleId: String? = null,
    /**
     * Optional custom Firebase Hosting domain to use for the action link.
     *
     * Replaces the former `dynamicLinkDomain`, since Firebase Dynamic Links has been shut down.
     * Maps to `ActionCodeSettings.Builder.setLinkDomain` (Android) and `FIRActionCodeSettings.linkDomain` (iOS).
     */
    val linkDomain: String? = null,
)
