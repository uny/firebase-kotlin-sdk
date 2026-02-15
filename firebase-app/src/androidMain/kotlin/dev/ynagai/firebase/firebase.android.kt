package dev.ynagai.firebase

import android.content.Context
import com.google.firebase.FirebaseApp as AndroidFirebaseApp

actual val Firebase.app: FirebaseApp
    get() = FirebaseApp(AndroidFirebaseApp.getInstance())

actual fun Firebase.initialize(context: Any?, options: FirebaseOptions, name: String): FirebaseApp =
    FirebaseApp(
        AndroidFirebaseApp.initializeApp(
            requireNotNull(context as? Context) { "Android Context is required. Pass an Activity or Application context." },
            options.toAndroid(),
            name
        )
    )

actual fun Firebase.app(name: String): FirebaseApp =
    FirebaseApp(AndroidFirebaseApp.getInstance(name))

actual fun Firebase.apps(context: Any?): List<FirebaseApp> =
    AndroidFirebaseApp.getApps(
        requireNotNull(context as? Context) { "Android Context is required. Pass an Activity or Application context." }
    ).map { FirebaseApp(it) }

actual class FirebaseApp(val android: AndroidFirebaseApp) {
    actual val name: String get() = android.name
    actual val options: FirebaseOptions get() = android.options.toCommon()
    actual suspend fun delete() = android.delete()
}

// Note: gaTrackingId is not set here because Android's FirebaseOptions.Builder
// does not expose a setGaTrackingId method. On Android, the GA tracking ID is
// configured exclusively via google-services.json.
internal fun FirebaseOptions.toAndroid(): com.google.firebase.FirebaseOptions =
    com.google.firebase.FirebaseOptions.Builder()
        .setApiKey(apiKey)
        .setApplicationId(applicationId)
        .apply {
            databaseUrl?.let { setDatabaseUrl(it) }
            gcmSenderId?.let { setGcmSenderId(it) }
            storageBucket?.let { setStorageBucket(it) }
            projectId?.let { setProjectId(it) }
        }
        .build()

internal fun com.google.firebase.FirebaseOptions.toCommon(): FirebaseOptions =
    FirebaseOptions(
        apiKey = apiKey,
        applicationId = applicationId,
        databaseUrl = databaseUrl,
        gaTrackingId = gaTrackingId,
        gcmSenderId = gcmSenderId,
        storageBucket = storageBucket,
        projectId = projectId,
    )
