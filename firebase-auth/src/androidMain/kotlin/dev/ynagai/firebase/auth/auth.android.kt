package dev.ynagai.firebase.auth

import com.google.firebase.auth.ActionCodeSettings as AndroidActionCodeSettings
import com.google.firebase.auth.FirebaseAuth as AndroidFirebaseAuth
import dev.ynagai.firebase.Firebase
import dev.ynagai.firebase.FirebaseApp
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

actual val Firebase.auth: FirebaseAuth
    get() = FirebaseAuth(AndroidFirebaseAuth.getInstance())

actual fun Firebase.auth(app: FirebaseApp): FirebaseAuth =
    FirebaseAuth(AndroidFirebaseAuth.getInstance(app.android))

actual class FirebaseAuth internal constructor(
    internal val android: AndroidFirebaseAuth,
) {
    actual val currentUser: FirebaseUser?
        get() = android.currentUser?.let { FirebaseUser(it) }

    actual var languageCode: String?
        get() = android.languageCode
        set(value) {
            if (value != null) {
                android.setLanguageCode(value)
            } else {
                android.useAppLanguage()
            }
        }

    actual fun useEmulator(host: String, port: Int) {
        android.useEmulator(host, port)
    }

    actual suspend fun signInWithEmailAndPassword(email: String, password: String): AuthResult =
        AuthResult(android.signInWithEmailAndPassword(email, password).await())

    actual suspend fun createUserWithEmailAndPassword(email: String, password: String): AuthResult =
        AuthResult(android.createUserWithEmailAndPassword(email, password).await())

    actual suspend fun signInAnonymously(): AuthResult =
        AuthResult(android.signInAnonymously().await())

    actual suspend fun signInWithCredential(credential: AuthCredential): AuthResult =
        AuthResult(android.signInWithCredential(credential.android).await())

    actual suspend fun signInWithCustomToken(token: String): AuthResult =
        AuthResult(android.signInWithCustomToken(token).await())

    actual fun signOut() {
        android.signOut()
    }

    actual suspend fun sendPasswordResetEmail(email: String) {
        android.sendPasswordResetEmail(email).await()
    }

    actual suspend fun sendSignInLinkToEmail(email: String, actionCodeSettings: ActionCodeSettings) {
        android.sendSignInLinkToEmail(email, actionCodeSettings.toAndroid()).await()
    }

    actual fun isSignInWithEmailLink(link: String): Boolean =
        android.isSignInWithEmailLink(link)

    actual suspend fun signInWithEmailLink(email: String, link: String): AuthResult =
        AuthResult(android.signInWithEmailLink(email, link).await())

    actual val authStateChanges: Flow<FirebaseUser?>
        get() = callbackFlow {
            val listener = AndroidFirebaseAuth.AuthStateListener { auth ->
                trySend(auth.currentUser?.let { FirebaseUser(it) })
            }
            android.addAuthStateListener(listener)
            awaitClose { android.removeAuthStateListener(listener) }
        }

    actual val idTokenChanges: Flow<FirebaseUser?>
        get() = callbackFlow {
            val listener = AndroidFirebaseAuth.IdTokenListener { auth ->
                trySend(auth.currentUser?.let { FirebaseUser(it) })
            }
            android.addIdTokenListener(listener)
            awaitClose { android.removeIdTokenListener(listener) }
        }
}

private fun ActionCodeSettings.toAndroid(): AndroidActionCodeSettings =
    AndroidActionCodeSettings.newBuilder()
        .setUrl(url)
        .setHandleCodeInApp(handleCodeInApp)
        .apply {
            androidPackageName?.let { setAndroidPackageName(it, androidInstallIfNotAvailable, androidMinimumVersion) }
            iOSBundleId?.let { setIOSBundleId(it) }
            dynamicLinkDomain?.let { setDynamicLinkDomain(it) }
        }
        .build()
