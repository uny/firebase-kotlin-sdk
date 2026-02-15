package dev.ynagai.firebase

object Firebase

/**
 * Returns the default [FirebaseApp] instance.
 *
 * Corresponds to `FirebaseApp.getInstance()` in the Android Firebase SDK.
 */
expect val Firebase.app: FirebaseApp

/**
 * Initializes a [FirebaseApp] with the given [options] and [name].
 *
 * Corresponds to `FirebaseApp.initializeApp(context, options, name)` in the Android Firebase SDK.
 * This library adopts Kotlin-idiomatic naming (`initialize`) instead of the Android SDK's
 * `initializeApp`.
 *
 * @param context On Android, an `Activity` or `Application` `Context` (required).
 *   On Apple platforms, this parameter is ignored and can be omitted.
 * @param options the [FirebaseOptions] to configure the app.
 * @param name the unique name for this app instance.
 * @return the newly initialized [FirebaseApp].
 */
expect fun Firebase.initialize(context: Any? = null, options: FirebaseOptions, name: String): FirebaseApp

/**
 * Returns the [FirebaseApp] instance identified by [name].
 *
 * Corresponds to `FirebaseApp.getInstance(name)` in the Android Firebase SDK.
 * This library adopts Kotlin-idiomatic naming (`app`) instead of the Android SDK's
 * `getInstance`.
 *
 * @param name the name of the app instance to retrieve.
 * @return the [FirebaseApp] with the given [name].
 * @throws IllegalStateException if no app with the given [name] exists.
 */
expect fun Firebase.app(name: String): FirebaseApp

/**
 * Returns all initialized [FirebaseApp] instances.
 *
 * Corresponds to `FirebaseApp.getApps(context)` in the Android Firebase SDK.
 * This library adopts Kotlin-idiomatic naming (`apps`) instead of the Android SDK's
 * `getApps`.
 *
 * @param context On Android, an `Activity` or `Application` `Context` (required).
 *   On Apple platforms, this parameter is ignored and can be omitted.
 * @return a list of all initialized [FirebaseApp] instances.
 */
expect fun Firebase.apps(context: Any? = null): List<FirebaseApp>

expect class FirebaseApp {
    val name: String
    val options: FirebaseOptions
    suspend fun delete()
}
