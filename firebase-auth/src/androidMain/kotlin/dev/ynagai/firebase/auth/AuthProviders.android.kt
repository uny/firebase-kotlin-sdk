package dev.ynagai.firebase.auth

import android.app.Activity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.EmailAuthProvider as AndroidEmailAuthProvider
import com.google.firebase.auth.FirebaseAuthException as AndroidFirebaseAuthException
import com.google.firebase.auth.GoogleAuthProvider as AndroidGoogleAuthProvider
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider as AndroidPhoneAuthProvider
import com.google.firebase.auth.FacebookAuthProvider as AndroidFacebookAuthProvider
import com.google.firebase.auth.GithubAuthProvider as AndroidGithubAuthProvider
import com.google.firebase.auth.OAuthProvider as AndroidOAuthProvider
import com.google.firebase.auth.TwitterAuthProvider as AndroidTwitterAuthProvider
import kotlinx.coroutines.suspendCancellableCoroutine
import java.lang.ref.WeakReference
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

actual class EmailAuthProvider {
    actual companion object {
        actual fun getCredential(email: String, password: String): AuthCredential =
            AuthCredential(AndroidEmailAuthProvider.getCredential(email, password))
    }
}

actual class GoogleAuthProvider {
    actual companion object {
        actual fun getCredential(idToken: String?, accessToken: String?): AuthCredential =
            AuthCredential(AndroidGoogleAuthProvider.getCredential(idToken, accessToken))
    }
}

actual class PhoneAuthProvider {
    actual companion object {
        private var activityRef: WeakReference<Activity>? = null

        fun initialize(activity: Activity) {
            activityRef = WeakReference(activity)
        }

        actual fun getCredential(verificationId: String, smsCode: String): AuthCredential =
            AuthCredential(AndroidPhoneAuthProvider.getCredential(verificationId, smsCode))

        actual suspend fun verifyPhoneNumber(
            auth: FirebaseAuth,
            phoneNumber: String,
        ): PhoneVerificationResult = suspendCancellableCoroutine { continuation ->
            val activity = activityRef?.get()
                ?: throw IllegalStateException(
                    "PhoneAuthProvider.initialize(activity) must be called before verifyPhoneNumber"
                )

            val resumed = AtomicBoolean(false)
            continuation.invokeOnCancellation { resumed.set(true) }

            val callbacks = object : AndroidPhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    if (resumed.compareAndSet(false, true)) {
                        continuation.resume(PhoneVerificationResult.AutoVerified(AuthCredential(credential)))
                    }
                }

                override fun onVerificationFailed(exception: FirebaseException) {
                    if (resumed.compareAndSet(false, true)) {
                        val authException = if (exception is AndroidFirebaseAuthException) {
                            exception.toCommon()
                        } else {
                            FirebaseAuthException(exception.message, FirebaseAuthExceptionCode.UNKNOWN)
                        }
                        continuation.resumeWithException(authException)
                    }
                }

                override fun onCodeSent(
                    verificationId: String,
                    token: AndroidPhoneAuthProvider.ForceResendingToken,
                ) {
                    if (resumed.compareAndSet(false, true)) {
                        continuation.resume(PhoneVerificationResult.CodeSent(verificationId))
                    }
                }
            }

            val options = PhoneAuthOptions.newBuilder(auth.android)
                .setPhoneNumber(phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(activity)
                .setCallbacks(callbacks)
                .build()

            AndroidPhoneAuthProvider.verifyPhoneNumber(options)
        }

        actual suspend fun verifyPhoneNumber(
            auth: FirebaseAuth,
            phoneNumber: String,
            session: MultiFactorSession,
        ): PhoneVerificationResult = suspendCancellableCoroutine { continuation ->
            val activity = activityRef?.get()
                ?: throw IllegalStateException(
                    "PhoneAuthProvider.initialize(activity) must be called before verifyPhoneNumber"
                )

            val resumed = AtomicBoolean(false)
            continuation.invokeOnCancellation { resumed.set(true) }

            val callbacks = object : AndroidPhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    if (resumed.compareAndSet(false, true)) {
                        continuation.resume(PhoneVerificationResult.AutoVerified(AuthCredential(credential)))
                    }
                }

                override fun onVerificationFailed(exception: FirebaseException) {
                    if (resumed.compareAndSet(false, true)) {
                        val authException = if (exception is AndroidFirebaseAuthException) {
                            exception.toCommon()
                        } else {
                            FirebaseAuthException(exception.message, FirebaseAuthExceptionCode.UNKNOWN)
                        }
                        continuation.resumeWithException(authException)
                    }
                }

                override fun onCodeSent(
                    verificationId: String,
                    token: AndroidPhoneAuthProvider.ForceResendingToken,
                ) {
                    if (resumed.compareAndSet(false, true)) {
                        continuation.resume(PhoneVerificationResult.CodeSent(verificationId))
                    }
                }
            }

            val options = PhoneAuthOptions.newBuilder(auth.android)
                .setPhoneNumber(phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(activity)
                .setMultiFactorSession(session.android)
                .setCallbacks(callbacks)
                .build()

            AndroidPhoneAuthProvider.verifyPhoneNumber(options)
        }
    }
}

actual class OAuthProvider {
    actual companion object {
        actual fun getCredential(
            providerId: String,
            idToken: String,
            accessToken: String?,
        ): AuthCredential {
            val builder = AndroidOAuthProvider.newCredentialBuilder(providerId)
            builder.setIdToken(idToken)
            accessToken?.let { builder.setAccessToken(it) }
            return AuthCredential(builder.build())
        }
    }
}

actual class FacebookAuthProvider {
    actual companion object {
        actual fun getCredential(accessToken: String): AuthCredential =
            AuthCredential(AndroidFacebookAuthProvider.getCredential(accessToken))
    }
}

actual class GithubAuthProvider {
    actual companion object {
        actual fun getCredential(token: String): AuthCredential =
            AuthCredential(AndroidGithubAuthProvider.getCredential(token))
    }
}

actual class TwitterAuthProvider {
    actual companion object {
        actual fun getCredential(token: String, secret: String): AuthCredential =
            AuthCredential(AndroidTwitterAuthProvider.getCredential(token, secret))
    }
}
