package dev.ynagai.firebase

import kotlinx.cinterop.ExperimentalForeignApi
import swiftPMImport.firebase.kotlin.sdk.firebase.app.FIRApp

@OptIn(ExperimentalForeignApi::class)
actual val Firebase.app: FirebaseApp
    get() = FirebaseApp(FIRApp.defaultApp()!!)

@OptIn(ExperimentalForeignApi::class)
actual class FirebaseApp(val apple: FIRApp)
