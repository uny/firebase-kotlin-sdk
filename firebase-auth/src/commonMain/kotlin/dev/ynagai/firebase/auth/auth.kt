package dev.ynagai.firebase.auth

import dev.ynagai.firebase.Firebase
import dev.ynagai.firebase.FirebaseApp
import kotlinx.coroutines.flow.Flow

expect val Firebase.auth: FirebaseAuth
expect fun Firebase.auth(app: FirebaseApp): FirebaseAuth

expect class FirebaseAuth {
    val currentUser: FirebaseUser?
    var languageCode: String?

    fun useEmulator(host: String, port: Int)

    suspend fun signInWithEmailAndPassword(email: String, password: String): AuthResult
    suspend fun createUserWithEmailAndPassword(email: String, password: String): AuthResult
    suspend fun signInAnonymously(): AuthResult
    suspend fun signInWithCredential(credential: AuthCredential): AuthResult
    suspend fun signInWithCustomToken(token: String): AuthResult

    fun signOut()

    suspend fun sendPasswordResetEmail(email: String)
    suspend fun sendPasswordResetEmail(email: String, actionCodeSettings: ActionCodeSettings)
    suspend fun checkActionCode(code: String): ActionCodeResult
    suspend fun applyActionCode(code: String)
    suspend fun confirmPasswordReset(code: String, newPassword: String)

    suspend fun sendSignInLinkToEmail(email: String, actionCodeSettings: ActionCodeSettings)
    fun isSignInWithEmailLink(link: String): Boolean
    suspend fun signInWithEmailLink(email: String, link: String): AuthResult

    suspend fun fetchSignInMethodsForEmail(email: String): List<String>

    val authStateChanges: Flow<FirebaseUser?>
    val idTokenChanges: Flow<FirebaseUser?>
}
