package dev.ynagai.firebase.auth

import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSURL
import swiftPMImport.dev.ynagai.firebase.firebase.auth.FIRUser
import swiftPMImport.dev.ynagai.firebase.firebase.auth.FIRUserInfoProtocol
import platform.Foundation.NSDate
import platform.Foundation.timeIntervalSince1970

@OptIn(ExperimentalForeignApi::class)
actual class FirebaseUser internal constructor(
    internal val apple: FIRUser,
) {
    actual val uid: String
        get() = apple.uid()

    actual val email: String?
        get() = apple.email()

    actual val displayName: String?
        get() = apple.displayName()

    actual val photoUrl: String?
        get() = apple.photoURL()?.absoluteString

    actual val phoneNumber: String?
        get() = apple.phoneNumber()

    actual val isAnonymous: Boolean
        get() = apple.isAnonymous()

    actual val isEmailVerified: Boolean
        get() = apple.isEmailVerified()

    actual val providerId: String
        get() = apple.providerID()

    actual val providerData: List<UserInfo>
        get() = apple.providerData().map { info ->
            val provider = info as FIRUserInfoProtocol
            UserInfo(
                uid = provider.uid(),
                providerId = provider.providerID(),
                displayName = provider.displayName(),
                email = provider.email(),
                phoneNumber = provider.phoneNumber(),
                photoUrl = provider.photoURL()?.absoluteString,
            )
        }

    actual val metadata: UserMetadata?
        get() {
            val m = apple.metadata()
            return UserMetadata(
                creationTimestamp = m.creationDate()?.toMillis(),
                lastSignInTimestamp = m.lastSignInDate()?.toMillis(),
            )
        }

    actual suspend fun delete() {
        await { callback -> apple.deleteWithCompletion(callback) }
    }

    actual suspend fun reload() {
        await { callback -> apple.reloadWithCompletion(callback) }
    }

    actual suspend fun getIdToken(forceRefresh: Boolean): String? =
        awaitResult { callback ->
            apple.getIDTokenForcingRefresh(forceRefresh) { token, error ->
                callback(token, error)
            }
        }

    actual suspend fun updatePassword(password: String) {
        await { callback -> apple.updatePassword(password, completion = callback) }
    }

    actual suspend fun updateProfile(displayName: String?, photoUrl: String?) {
        val changeRequest = apple.profileChangeRequest()
        changeRequest.setDisplayName(displayName)
        changeRequest.setPhotoURL(photoUrl?.let { NSURL.URLWithString(it) })
        await { callback -> changeRequest.commitChangesWithCompletion(callback) }
    }

    actual suspend fun sendEmailVerification() {
        await { callback -> apple.sendEmailVerificationWithCompletion(callback) }
    }

    actual suspend fun sendEmailVerification(actionCodeSettings: ActionCodeSettings) {
        await { callback ->
            apple.sendEmailVerificationWithActionCodeSettings(actionCodeSettings.toApple(), completion = callback)
        }
    }

    actual suspend fun linkWithCredential(credential: AuthCredential): AuthResult =
        AuthResult(
            awaitResult { callback ->
                apple.linkWithCredential(credential.apple, completion = callback)
            }
        )

    actual suspend fun unlink(providerId: String): FirebaseUser =
        FirebaseUser(
            awaitResult { callback ->
                apple.unlinkFromProvider(providerId, completion = callback)
            }
        )

    actual suspend fun reauthenticate(credential: AuthCredential) {
        await { callback ->
            apple.reauthenticateWithCredential(credential.apple) { _, error ->
                callback(error)
            }
        }
    }

    actual suspend fun verifyBeforeUpdateEmail(newEmail: String) {
        await { callback ->
            apple.sendEmailVerificationBeforeUpdatingEmail(newEmail, completion = callback)
        }
    }
}

private fun NSDate.toMillis(): Long = (timeIntervalSince1970 * 1000).toLong()
