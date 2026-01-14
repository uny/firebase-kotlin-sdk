package dev.ynagai.firebase

import FirebaseAppBridge.FirebaseApp as AppleFirebaseApp

actual val Firebase.app: FirebaseApp
    get() = FirebaseApp(AppleFirebaseApp())

actual class FirebaseApp(val apple: AppleFirebaseApp)
