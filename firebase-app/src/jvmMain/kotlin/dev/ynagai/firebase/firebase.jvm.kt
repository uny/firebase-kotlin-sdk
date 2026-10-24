package dev.ynagai.firebase

// JVM has no official Firebase client SDK. This target is an in-memory fake intended for
// desktop previews and tests: apps are kept in a process-local registry and nothing is sent
// over the network. Lookups follow the Android SDK and throw when the app is not initialized.

private const val DEFAULT_APP_NAME = "[DEFAULT]"

private val apps = mutableMapOf<String, FirebaseApp>()

actual val Firebase.app: FirebaseApp
    get() = app(DEFAULT_APP_NAME)

actual fun Firebase.initialize(context: Any?, options: FirebaseOptions, name: String): FirebaseApp =
    synchronized(apps) {
        check(name !in apps) { "FirebaseApp name $name already exists!" }
        FirebaseApp(name, options).also { apps[name] = it }
    }

actual fun Firebase.app(name: String): FirebaseApp =
    synchronized(apps) {
        checkNotNull(apps[name]) {
            "FirebaseApp with name $name doesn't exist. Call Firebase.initialize() first."
        }
    }

actual fun Firebase.apps(context: Any?): List<FirebaseApp> =
    synchronized(apps) { apps.values.toList() }

actual class FirebaseApp internal constructor(
    actual val name: String,
    actual val options: FirebaseOptions,
) {
    actual suspend fun delete() {
        synchronized(apps) { apps.remove(name) }
    }
}
