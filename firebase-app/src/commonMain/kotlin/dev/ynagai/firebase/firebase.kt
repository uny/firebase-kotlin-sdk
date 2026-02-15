package dev.ynagai.firebase

object Firebase

expect val Firebase.app: FirebaseApp

expect class FirebaseApp {
    val name: String
    val options: FirebaseOptions
}
