package dev.ynagai.firebase

import kotlinx.cinterop.ExperimentalForeignApi
import swiftPMImport.dev.ynagai.firebase.firebase.app.FIRApp

@OptIn(ExperimentalForeignApi::class)
actual val Firebase.app: FirebaseApp
    get() = FirebaseApp(
        requireNotNull(FIRApp.defaultApp()) {
            "Default FirebaseApp not initialized. Call FirebaseApp.configure() in your AppDelegate."
        }
    )

@OptIn(ExperimentalForeignApi::class)
actual class FirebaseApp(val apple: FIRApp)
