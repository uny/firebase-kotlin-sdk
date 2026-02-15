package dev.ynagai.firebase

import kotlinx.cinterop.ExperimentalForeignApi
import swiftPMImport.dev.ynagai.firebase.firebase.app.FIRApp
import swiftPMImport.dev.ynagai.firebase.firebase.app.FIROptions

@OptIn(ExperimentalForeignApi::class)
actual val Firebase.app: FirebaseApp
    get() = FirebaseApp(
        requireNotNull(FIRApp.defaultApp()) {
            "Default FirebaseApp not initialized. Call FirebaseApp.configure() in your AppDelegate."
        }
    )

@OptIn(ExperimentalForeignApi::class)
actual class FirebaseApp(val apple: FIRApp) {
    actual val name: String get() = apple.name
    actual val options: FirebaseOptions get() = apple.options.toCommon()
}

@OptIn(ExperimentalForeignApi::class)
internal fun FIROptions.toCommon(): FirebaseOptions =
    FirebaseOptions(
        apiKey = APIKey ?: "",
        applicationId = googleAppID,
        databaseUrl = databaseURL,
        gcmSenderId = GCMSenderID,
        storageBucket = storageBucket,
        projectId = projectID,
    )
