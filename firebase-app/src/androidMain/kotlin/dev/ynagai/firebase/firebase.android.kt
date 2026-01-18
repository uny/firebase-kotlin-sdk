package dev.ynagai.firebase

import com.google.firebase.FirebaseApp as AndroidFirebaseApp

actual val Firebase.app: FirebaseApp
    get() = FirebaseApp(AndroidFirebaseApp.getInstance())

actual class FirebaseApp(val android: AndroidFirebaseApp)
