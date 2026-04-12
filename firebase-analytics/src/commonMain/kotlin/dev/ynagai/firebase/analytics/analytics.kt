package dev.ynagai.firebase.analytics

import dev.ynagai.firebase.Firebase
import dev.ynagai.firebase.FirebaseApp

expect val Firebase.analytics: FirebaseAnalytics
expect fun Firebase.analytics(app: FirebaseApp): FirebaseAnalytics

expect class FirebaseAnalytics {
    fun logEvent(name: String, params: Map<String, Any>? = null)

    fun setUserId(id: String?)

    fun setUserProperty(name: String, value: String?)

    fun setAnalyticsCollectionEnabled(enabled: Boolean)

    fun resetAnalyticsData()

    suspend fun getAppInstanceId(): String?

    suspend fun getSessionId(): Long?

    fun setDefaultEventParameters(params: Map<String, Any>?)

    fun setConsent(consentSettings: Map<ConsentType, ConsentStatus>)

    fun setSessionTimeoutDuration(milliseconds: Long)
}
