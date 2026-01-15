package dev.ynagai.firebase

import dev.ynagai.firebase.app.cinterop.KTFFirebaseApp
import kotlinx.cinterop.ExperimentalForeignApi

@OptIn(ExperimentalForeignApi::class)
actual val Firebase.app: FirebaseApp
    get() = FirebaseApp(KTFFirebaseApp.app()!!)

@OptIn(ExperimentalForeignApi::class)
actual class FirebaseApp(val apple: KTFFirebaseApp)
