package dev.ynagai.firebase

data class FirebaseOptions(
    val apiKey: String,
    val applicationId: String,
    val databaseUrl: String? = null,
    val gaTrackingId: String? = null,
    val gcmSenderId: String? = null,
    val storageBucket: String? = null,
    val projectId: String? = null,
)
