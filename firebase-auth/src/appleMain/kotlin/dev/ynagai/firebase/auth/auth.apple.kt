package dev.ynagai.firebase.auth

import dev.ynagai.firebase.Firebase
import dev.ynagai.firebase.FirebaseApp
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCObjectVar
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.value
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import platform.Foundation.NSError
import swiftPMImport.dev.ynagai.firebase.firebase.auth.FIRAuth
import swiftPMImport.dev.ynagai.firebase.firebase.auth.FIRUser

@OptIn(ExperimentalForeignApi::class)
actual val Firebase.auth: FirebaseAuth
    get() = FirebaseAuth(FIRAuth.auth())

@OptIn(ExperimentalForeignApi::class)
actual fun Firebase.auth(app: FirebaseApp): FirebaseAuth =
    FirebaseAuth(FIRAuth.authWithApp(app.apple))

@OptIn(ExperimentalForeignApi::class)
actual class FirebaseAuth internal constructor(
    internal val apple: FIRAuth,
) {
    actual val currentUser: FirebaseUser?
        get() = apple.currentUser()?.let { FirebaseUser(it) }

    actual var languageCode: String?
        get() = apple.languageCode()
        set(value) {
            apple.setLanguageCode(value)
            if (value == null) {
                apple.useAppLanguage()
            }
        }

    actual fun useEmulator(host: String, port: Int) {
        apple.useEmulatorWithHost(host, port.toLong())
    }

    actual suspend fun signInWithEmailAndPassword(email: String, password: String): AuthResult =
        AuthResult(
            awaitResult { callback ->
                apple.signInWithEmail(email, password = password, completion = callback)
            }
        )

    actual suspend fun createUserWithEmailAndPassword(email: String, password: String): AuthResult =
        AuthResult(
            awaitResult { callback ->
                apple.createUserWithEmail(email, password = password, completion = callback)
            }
        )

    actual suspend fun signInAnonymously(): AuthResult =
        AuthResult(
            awaitResult { callback ->
                apple.signInAnonymouslyWithCompletion(callback)
            }
        )

    actual suspend fun signInWithCredential(credential: AuthCredential): AuthResult =
        AuthResult(
            awaitResult { callback ->
                apple.signInWithCredential(credential.apple, completion = callback)
            }
        )

    actual suspend fun signInWithCustomToken(token: String): AuthResult =
        AuthResult(
            awaitResult { callback ->
                apple.signInWithCustomToken(token, completion = callback)
            }
        )

    @OptIn(BetaInteropApi::class)
    actual fun signOut() {
        memScoped {
            val errorPtr = alloc<ObjCObjectVar<NSError?>>()
            apple.signOut(errorPtr.ptr)
            errorPtr.value?.let { throw it.toAuthException() }
        }
    }

    actual suspend fun sendPasswordResetEmail(email: String) {
        await { callback -> apple.sendPasswordResetWithEmail(email, completion = callback) }
    }

    actual val authStateChanges: Flow<FirebaseUser?>
        get() = callbackFlow {
            val handle = apple.addAuthStateDidChangeListener { _, user ->
                trySend((user as? FIRUser)?.let { FirebaseUser(it) })
            }
            awaitClose { apple.removeAuthStateDidChangeListener(handle) }
        }

    actual val idTokenChanges: Flow<FirebaseUser?>
        get() = callbackFlow {
            val handle = apple.addIDTokenDidChangeListener { _, user ->
                trySend((user as? FIRUser)?.let { FirebaseUser(it) })
            }
            awaitClose { apple.removeIDTokenDidChangeListener(handle) }
        }
}
