package dev.ynagai.firebase

object Firebase

expect val Firebase.app: FirebaseApp

expect fun Firebase.initialize(context: Any? = null, options: FirebaseOptions, name: String): FirebaseApp

expect fun Firebase.app(name: String): FirebaseApp

expect fun Firebase.apps(context: Any? = null): List<FirebaseApp>

expect class FirebaseApp {
    val name: String
    val options: FirebaseOptions
    fun delete()
}
