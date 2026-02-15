package dev.ynagai.firebase

object Firebase

expect val Firebase.app: FirebaseApp

expect fun Firebase.initialize(options: FirebaseOptions, name: String): FirebaseApp

expect fun Firebase.app(name: String): FirebaseApp

expect val Firebase.apps: List<FirebaseApp>

expect class FirebaseApp {
    val name: String
    val options: FirebaseOptions
    fun delete()
}
