package dev.ynagai.firebase

import com.google.firebase.FirebaseApp as AndroidFirebaseApp

actual val Firebase.app: FirebaseApp
    get() = FirebaseApp(AndroidFirebaseApp.getInstance())

actual class FirebaseApp(val android: AndroidFirebaseApp) {
    actual val name: String get() = android.name
    actual val options: FirebaseOptions get() = android.options.toCommon()
}

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
