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
import platform.Foundation.NSURL
import swiftPMImport.dev.ynagai.firebase.firebase.auth.FIRActionCodeSettings
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
            if (value != null) {
                apple.setLanguageCode(value)
            } else {
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

    actual suspend fun sendPasswordResetEmail(email: String, actionCodeSettings: ActionCodeSettings) {
        await { callback ->
            apple.sendPasswordResetWithEmail(email, actionCodeSettings = actionCodeSettings.toApple(), completion = callback)
        }
    }

    actual suspend fun checkActionCode(code: String): ActionCodeResult {
        val info = awaitResult { callback ->
            apple.checkActionCode(code, completion = callback)
        }
        return ActionCodeResult(
            operation = info.operation().toActionCodeOperation(),
            email = info.email(),
            previousEmail = info.previousEmail(),
        )
    }

    actual suspend fun applyActionCode(code: String) {
        await { callback -> apple.applyActionCode(code, completion = callback) }
    }

    actual suspend fun confirmPasswordReset(code: String, newPassword: String) {
        await { callback -> apple.confirmPasswordResetWithCode(code, newPassword = newPassword, completion = callback) }
    }

    actual suspend fun sendSignInLinkToEmail(email: String, actionCodeSettings: ActionCodeSettings) {
        await { callback ->
            apple.sendSignInLinkToEmail(email, actionCodeSettings = actionCodeSettings.toApple(), completion = callback)
        }
    }

    actual fun isSignInWithEmailLink(link: String): Boolean =
        apple.isSignInWithEmailLink(link)

    actual suspend fun signInWithEmailLink(email: String, link: String): AuthResult =
        AuthResult(
            awaitResult { callback ->
                apple.signInWithEmail(email, link = link, completion = callback)
            }
        )

    @Suppress("UNCHECKED_CAST")
    actual suspend fun fetchSignInMethodsForEmail(email: String): List<String> =
        awaitResult<List<*>> { callback ->
            apple.fetchSignInMethodsForEmail(email, completion = callback)
        } as List<String>

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

// FIRActionCodeOperation raw values from Firebase iOS SDK
private fun Long.toActionCodeOperation(): ActionCodeOperation = when (this) {
    1L -> ActionCodeOperation.PASSWORD_RESET
    2L -> ActionCodeOperation.VERIFY_EMAIL
    3L -> ActionCodeOperation.RECOVER_EMAIL
    4L -> ActionCodeOperation.SIGN_IN_WITH_EMAIL_LINK
    5L -> ActionCodeOperation.VERIFY_AND_CHANGE_EMAIL
    6L -> ActionCodeOperation.REVERT_SECOND_FACTOR_ADDITION
    else -> ActionCodeOperation.UNKNOWN
}

@OptIn(ExperimentalForeignApi::class)
internal fun ActionCodeSettings.toApple(): FIRActionCodeSettings =
    FIRActionCodeSettings().apply {
        setURL(NSURL(string = url))
        setHandleCodeInApp(handleCodeInApp)
        androidPackageName?.let {
            setAndroidPackageName(it, installIfNotAvailable = androidInstallIfNotAvailable, minimumVersion = androidMinimumVersion)
        }
        iOSBundleId?.let { setIOSBundleID(it) }
        dynamicLinkDomain?.let { setLinkDomain(it) }
    }
