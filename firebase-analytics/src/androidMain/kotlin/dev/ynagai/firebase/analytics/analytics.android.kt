package dev.ynagai.firebase.analytics

import android.os.Bundle
import com.google.firebase.FirebaseApp as AndroidFirebaseApp
import com.google.firebase.analytics.FirebaseAnalytics as AndroidFirebaseAnalytics
import com.google.firebase.analytics.FirebaseAnalytics.ConsentStatus as AndroidConsentStatus
import com.google.firebase.analytics.FirebaseAnalytics.ConsentType as AndroidConsentType
import dev.ynagai.firebase.Firebase
import dev.ynagai.firebase.FirebaseApp
import kotlinx.coroutines.tasks.await

actual val Firebase.analytics: FirebaseAnalytics
    get() = FirebaseAnalytics(
        AndroidFirebaseAnalytics.getInstance(AndroidFirebaseApp.getInstance().applicationContext)
    )

actual fun Firebase.analytics(app: FirebaseApp): FirebaseAnalytics =
    FirebaseAnalytics(
        AndroidFirebaseAnalytics.getInstance(app.android.applicationContext)
    )

actual class FirebaseAnalytics internal constructor(
    internal val android: AndroidFirebaseAnalytics,
) {
    actual fun logEvent(name: String, params: Map<String, Any>?) {
        android.logEvent(name, params?.toBundle())
    }

    actual fun setUserId(id: String?) {
        android.setUserId(id)
    }

    actual fun setUserProperty(name: String, value: String?) {
        android.setUserProperty(name, value)
    }

    actual fun setAnalyticsCollectionEnabled(enabled: Boolean) {
        android.setAnalyticsCollectionEnabled(enabled)
    }

    actual fun resetAnalyticsData() {
        android.resetAnalyticsData()
    }

    actual suspend fun getAppInstanceId(): String? =
        android.appInstanceId.await()

    actual suspend fun getSessionId(): Long? =
        android.sessionId.await()

    actual fun setDefaultEventParameters(params: Map<String, Any>?) {
        android.setDefaultEventParameters(params?.toBundle())
    }

    actual fun setConsent(consentSettings: Map<ConsentType, ConsentStatus>) {
        android.setConsent(
            consentSettings.map { (type, status) ->
                type.toAndroid() to status.toAndroid()
            }.toMap()
        )
    }

    actual fun setSessionTimeoutDuration(milliseconds: Long) {
        android.setSessionTimeoutDuration(milliseconds)
    }
}

private fun Map<String, Any>.toBundle(): Bundle =
    Bundle().apply {
        for ((key, value) in this@toBundle) {
            when (value) {
                is String -> putString(key, value)
                is Int -> putInt(key, value)
                is Long -> putLong(key, value)
                is Float -> putFloat(key, value)
                is Double -> putDouble(key, value)
                is Boolean -> putBoolean(key, value)
            }
        }
    }

private fun ConsentType.toAndroid(): AndroidConsentType = when (this) {
    ConsentType.AD_PERSONALIZATION -> AndroidConsentType.AD_PERSONALIZATION
    ConsentType.AD_STORAGE -> AndroidConsentType.AD_STORAGE
    ConsentType.AD_USER_DATA -> AndroidConsentType.AD_USER_DATA
    ConsentType.ANALYTICS_STORAGE -> AndroidConsentType.ANALYTICS_STORAGE
}

private fun ConsentStatus.toAndroid(): AndroidConsentStatus = when (this) {
    ConsentStatus.GRANTED -> AndroidConsentStatus.GRANTED
    ConsentStatus.DENIED -> AndroidConsentStatus.DENIED
}
