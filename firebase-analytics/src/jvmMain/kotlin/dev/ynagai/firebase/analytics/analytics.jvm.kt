package dev.ynagai.firebase.analytics

import dev.ynagai.firebase.Firebase
import dev.ynagai.firebase.FirebaseApp
import dev.ynagai.firebase.app

// JVM has no official Firebase client SDK. This target is a no-op fake intended for desktop
// previews and tests: every call is accepted and nothing is recorded or sent.

actual val Firebase.analytics: FirebaseAnalytics
    get() = FirebaseAnalytics(app)

actual fun Firebase.analytics(app: FirebaseApp): FirebaseAnalytics = FirebaseAnalytics(app)

@Suppress("UNUSED_PARAMETER")
actual class FirebaseAnalytics internal constructor(app: FirebaseApp) {
    actual fun logEvent(name: String, params: Map<String, Any>?) = Unit

    actual fun setUserId(id: String?) = Unit

    actual fun setUserProperty(name: String, value: String?) = Unit

    actual fun setAnalyticsCollectionEnabled(enabled: Boolean) = Unit

    actual fun resetAnalyticsData() = Unit

    actual suspend fun getAppInstanceId(): String? = null

    actual suspend fun getSessionId(): Long? = null

    actual fun setDefaultEventParameters(params: Map<String, Any>?) = Unit

    actual fun setConsent(consentSettings: Map<ConsentType, ConsentStatus>) = Unit

    actual fun setSessionTimeoutDuration(milliseconds: Long) = Unit
}
