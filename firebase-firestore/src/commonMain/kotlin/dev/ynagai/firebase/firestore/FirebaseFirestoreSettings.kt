package dev.ynagai.firebase.firestore

/**
 * Settings for [FirebaseFirestore] instances.
 *
 * Use [firestoreSettings] DSL function to create instances.
 *
 * @property host The host of the Firestore backend.
 * @property isSslEnabled Whether to use SSL/TLS to connect.
 * @property isPersistenceEnabled Whether to enable local persistence.
 * @property cacheSizeBytes The threshold for cache size above which the SDK will attempt to
 *                          garbage collect. Use [CACHE_SIZE_UNLIMITED] to disable garbage collection.
 */
data class FirebaseFirestoreSettings(
    val host: String = DEFAULT_HOST,
    val isSslEnabled: Boolean = true,
    val isPersistenceEnabled: Boolean = true,
    val cacheSizeBytes: Long = DEFAULT_CACHE_SIZE_BYTES,
) {
    companion object {
        /** Constant to indicate that cache garbage collection is disabled. */
        const val CACHE_SIZE_UNLIMITED: Long = -1L

        /** Default Firestore host. */
        const val DEFAULT_HOST: String = "firestore.googleapis.com"

        internal const val DEFAULT_CACHE_SIZE_BYTES: Long = 100L * 1024 * 1024 // 100 MB
    }
}

/** DSL marker for the Firestore settings builder. */
@DslMarker
annotation class FirestoreSettingsDsl

/**
 * Builder class for constructing [FirebaseFirestoreSettings] instances using a DSL.
 *
 * @see firestoreSettings
 */
@FirestoreSettingsDsl
class FirebaseFirestoreSettingsBuilder {
    /** The host of the Firestore backend. */
    var host: String = FirebaseFirestoreSettings.DEFAULT_HOST
    /** Whether to use SSL/TLS to connect. */
    var isSslEnabled: Boolean = true
    /** Whether to enable local persistence. */
    var isPersistenceEnabled: Boolean = true
    /** The threshold for cache size above which the SDK will attempt to garbage collect. */
    var cacheSizeBytes: Long = FirebaseFirestoreSettings.DEFAULT_CACHE_SIZE_BYTES

    internal fun build(): FirebaseFirestoreSettings = FirebaseFirestoreSettings(
        host = host,
        isSslEnabled = isSslEnabled,
        isPersistenceEnabled = isPersistenceEnabled,
        cacheSizeBytes = cacheSizeBytes,
    )
}

/**
 * Creates a [FirebaseFirestoreSettings] using a DSL builder.
 *
 * @param block The builder block for configuring Firestore settings.
 * @return A new [FirebaseFirestoreSettings] instance.
 *
 * @sample
 * ```kotlin
 * val settings = firestoreSettings {
 *     host = "10.0.2.2:8080"
 *     isSslEnabled = false
 *     isPersistenceEnabled = true
 *     cacheSizeBytes = FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED
 * }
 *
 * Firebase.firestore.settings = settings
 * ```
 */
fun firestoreSettings(block: FirebaseFirestoreSettingsBuilder.() -> Unit): FirebaseFirestoreSettings {
    val builder = FirebaseFirestoreSettingsBuilder()
    builder.block()
    return builder.build()
}
