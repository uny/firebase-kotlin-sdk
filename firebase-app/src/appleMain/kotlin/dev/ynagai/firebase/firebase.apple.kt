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
actual fun Firebase.initialize(context: Any?, options: FirebaseOptions, name: String): FirebaseApp {
    val firOptions = options.toApple()
    FIRApp.configureWithName(name, options = firOptions)
    return FirebaseApp(requireNotNull(FIRApp.appNamed(name)))
}

@OptIn(ExperimentalForeignApi::class)
actual fun Firebase.app(name: String): FirebaseApp =
    FirebaseApp(requireNotNull(FIRApp.appNamed(name)) {
        "FirebaseApp named '$name' not found."
    })

@OptIn(ExperimentalForeignApi::class)
actual fun Firebase.apps(context: Any?): List<FirebaseApp> =
    FIRApp.allApps?.values?.map { FirebaseApp(it as FIRApp) } ?: emptyList()

@OptIn(ExperimentalForeignApi::class)
actual class FirebaseApp(val apple: FIRApp) {
    actual val name: String get() = apple.name
    actual val options: FirebaseOptions get() = apple.options.toCommon()
    actual fun delete() = apple.deleteApp { }
}

@OptIn(ExperimentalForeignApi::class)
internal fun FirebaseOptions.toApple(): FIROptions =
    FIROptions(googleAppID = applicationId, GCMSenderID = gcmSenderId ?: "").apply {
        APIKey = this@toApple.apiKey
        databaseURL = this@toApple.databaseUrl
        storageBucket = this@toApple.storageBucket
        projectID = this@toApple.projectId
    }

@OptIn(ExperimentalForeignApi::class)
internal fun FIROptions.toCommon(): FirebaseOptions =
    FirebaseOptions(
        apiKey = requireNotNull(APIKey) {
            "FIROptions.APIKey is missing. Ensure FirebaseApp.configure() was called with valid options."
        },
        applicationId = googleAppID,
        databaseUrl = databaseURL,
        gcmSenderId = GCMSenderID,
        storageBucket = storageBucket,
        projectId = projectID,
    )
