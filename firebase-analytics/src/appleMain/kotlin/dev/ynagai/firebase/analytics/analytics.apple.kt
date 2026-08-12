package dev.ynagai.firebase.analytics

import dev.ynagai.firebase.Firebase
import dev.ynagai.firebase.FirebaseApp
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.suspendCancellableCoroutine
import swiftPMImport.dev.ynagai.firebase.firebase.analytics.FIRAnalytics
import swiftPMImport.dev.ynagai.firebase.firebase.analytics.FIRConsentStatusDenied
import swiftPMImport.dev.ynagai.firebase.firebase.analytics.FIRConsentStatusGranted
import swiftPMImport.dev.ynagai.firebase.firebase.analytics.FIRConsentTypeAdPersonalization
import swiftPMImport.dev.ynagai.firebase.firebase.analytics.FIRConsentTypeAdStorage
import swiftPMImport.dev.ynagai.firebase.firebase.analytics.FIRConsentTypeAdUserData
import swiftPMImport.dev.ynagai.firebase.firebase.analytics.FIRConsentTypeAnalyticsStorage
import swiftPMImport.dev.ynagai.firebase.firebase.analytics.setConsent
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@OptIn(ExperimentalForeignApi::class)
actual val Firebase.analytics: FirebaseAnalytics
    get() = FirebaseAnalytics()

// FIRAnalytics is a class-method-only API with no multi-app support on iOS.
// Always returns the default-app singleton regardless of which app is passed.
@OptIn(ExperimentalForeignApi::class)
actual fun Firebase.analytics(app: FirebaseApp): FirebaseAnalytics =
    FirebaseAnalytics()

@OptIn(ExperimentalForeignApi::class)
actual class FirebaseAnalytics internal constructor() {
    @Suppress("UNCHECKED_CAST")
    actual fun logEvent(name: String, params: Map<String, Any>?) {
        FIRAnalytics.logEventWithName(name, parameters = params as Map<Any?, *>?)
    }

    actual fun setUserId(id: String?) {
        FIRAnalytics.setUserID(id)
    }

    actual fun setUserProperty(name: String, value: String?) {
        FIRAnalytics.setUserPropertyString(value, forName = name)
    }

    actual fun setAnalyticsCollectionEnabled(enabled: Boolean) {
        FIRAnalytics.setAnalyticsCollectionEnabled(enabled)
    }

    actual fun resetAnalyticsData() {
        FIRAnalytics.resetAnalyticsData()
    }

    actual suspend fun getAppInstanceId(): String? =
        FIRAnalytics.appInstanceID()

    actual suspend fun getSessionId(): Long? =
        suspendCancellableCoroutine { continuation ->
            FIRAnalytics.sessionIDWithCompletion { sessionId, error ->
                if (!continuation.isActive) return@sessionIDWithCompletion
                if (error != null) {
                    continuation.resumeWithException(Exception(error.localizedDescription))
                } else {
                    continuation.resume(sessionId)
                }
            }
        }

    @Suppress("UNCHECKED_CAST")
    actual fun setDefaultEventParameters(params: Map<String, Any>?) {
        FIRAnalytics.setDefaultEventParameters(params as Map<Any?, *>?)
    }

    actual fun setConsent(consentSettings: Map<ConsentType, ConsentStatus>) {
        FIRAnalytics.setConsent(
            buildMap<Any?, Any> {
                consentSettings.forEach { (type, status) ->
                    put(type.toApple(), status.toApple())
                }
            },
        )
    }

    actual fun setSessionTimeoutDuration(milliseconds: Long) {
        FIRAnalytics.setSessionTimeoutInterval(milliseconds.toDouble() / 1000.0)
    }
}

@OptIn(ExperimentalForeignApi::class)
internal fun ConsentType.toApple(): Any = when (this) {
    ConsentType.AD_PERSONALIZATION -> FIRConsentTypeAdPersonalization!!
    ConsentType.AD_STORAGE -> FIRConsentTypeAdStorage!!
    ConsentType.AD_USER_DATA -> FIRConsentTypeAdUserData!!
    ConsentType.ANALYTICS_STORAGE -> FIRConsentTypeAnalyticsStorage!!
}

@OptIn(ExperimentalForeignApi::class)
internal fun ConsentStatus.toApple(): Any = when (this) {
    ConsentStatus.GRANTED -> FIRConsentStatusGranted!!
    ConsentStatus.DENIED -> FIRConsentStatusDenied!!
}
