package dev.ynagai.firebase.auth

import android.net.Uri
import com.google.firebase.auth.FirebaseUser as AndroidFirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.tasks.await

actual class FirebaseUser internal constructor(
    internal val android: AndroidFirebaseUser,
) {
    actual val uid: String
        get() = android.uid

    actual val email: String?
        get() = android.email

    actual val displayName: String?
        get() = android.displayName

    actual val photoUrl: String?
        get() = android.photoUrl?.toString()

    actual val phoneNumber: String?
        get() = android.phoneNumber

    actual val isAnonymous: Boolean
        get() = android.isAnonymous

    actual val isEmailVerified: Boolean
        get() = android.isEmailVerified

    actual val providerId: String
        get() = android.providerId

    actual val providerData: List<UserInfo>
        get() = android.providerData.map { info ->
            UserInfo(
                uid = info.uid ?: "",
                providerId = info.providerId,
                displayName = info.displayName,
                email = info.email,
                phoneNumber = info.phoneNumber,
                photoUrl = info.photoUrl?.toString(),
            )
        }

    actual val metadata: UserMetadata?
        get() = android.metadata?.let {
            UserMetadata(
                creationTimestamp = it.creationTimestamp,
                lastSignInTimestamp = it.lastSignInTimestamp,
            )
        }

    actual suspend fun delete() {
        android.delete().await()
    }

    actual suspend fun reload() {
        android.reload().await()
    }

    actual suspend fun getIdToken(forceRefresh: Boolean): String? =
        android.getIdToken(forceRefresh).await().token

    actual suspend fun updatePassword(password: String) {
        android.updatePassword(password).await()
    }

    actual suspend fun updateProfile(displayName: String?, photoUrl: String?) {
        val request = UserProfileChangeRequest.Builder()
            .setDisplayName(displayName)
            .setPhotoUri(photoUrl?.let { Uri.parse(it) })
            .build()
        android.updateProfile(request).await()
    }

    actual suspend fun sendEmailVerification() {
        android.sendEmailVerification().await()
    }

    actual suspend fun sendEmailVerification(actionCodeSettings: ActionCodeSettings) {
        android.sendEmailVerification(actionCodeSettings.toAndroid()).await()
    }

    actual suspend fun linkWithCredential(credential: AuthCredential): AuthResult =
        AuthResult(android.linkWithCredential(credential.android).await())

    actual suspend fun unlink(providerId: String): FirebaseUser =
        FirebaseUser(android.unlink(providerId).await().user!!)

    actual suspend fun reauthenticate(credential: AuthCredential) {
        android.reauthenticate(credential.android).await()
    }

    actual suspend fun verifyBeforeUpdateEmail(newEmail: String) {
        android.verifyBeforeUpdateEmail(newEmail).await()
    }
}
