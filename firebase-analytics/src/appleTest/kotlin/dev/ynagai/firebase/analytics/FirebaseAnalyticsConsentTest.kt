package dev.ynagai.firebase.analytics

import kotlinx.cinterop.ExperimentalForeignApi
import swiftPMImport.dev.ynagai.firebase.firebase.analytics.FIRConsentStatusDenied
import swiftPMImport.dev.ynagai.firebase.firebase.analytics.FIRConsentStatusGranted
import swiftPMImport.dev.ynagai.firebase.firebase.analytics.FIRConsentTypeAdPersonalization
import swiftPMImport.dev.ynagai.firebase.firebase.analytics.FIRConsentTypeAdStorage
import swiftPMImport.dev.ynagai.firebase.firebase.analytics.FIRConsentTypeAdUserData
import swiftPMImport.dev.ynagai.firebase.firebase.analytics.FIRConsentTypeAnalyticsStorage
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalForeignApi::class)
class FirebaseAnalyticsConsentTest {

    // Regression: setConsent used to be bridged through
    // -[NSObject performSelector:withObject:], whose binding returns `id`.
    // +[FIRAnalytics setConsent:] returns void, so the interop layer converted
    // an undefined return register into an ObjC reference and segfaulted.
    // Runs against the real SDK, so a return-type regression crashes the test
    // process rather than merely failing an assertion.
    @Test
    fun setConsentDoesNotCrash() {
        FirebaseAnalytics().setConsent(
            mapOf(
                ConsentType.AD_STORAGE to ConsentStatus.DENIED,
                ConsentType.AD_USER_DATA to ConsentStatus.DENIED,
                ConsentType.AD_PERSONALIZATION to ConsentStatus.DENIED,
                ConsentType.ANALYTICS_STORAGE to ConsentStatus.GRANTED,
            ),
        )
    }

    // FIRAnalytics exposes no consent getter, so the dictionary handed to
    // setConsent cannot be read back. Pin the mapping instead: a wrong constant
    // collapses two keys into one, and setConsent then silently drops a consent
    // type without crashing.
    @Test
    fun consentTypesMapToTheirOwnAppleConstants() {
        assertEquals<Any?>(FIRConsentTypeAdStorage, ConsentType.AD_STORAGE.toApple())
        assertEquals<Any?>(FIRConsentTypeAdUserData, ConsentType.AD_USER_DATA.toApple())
        assertEquals<Any?>(FIRConsentTypeAdPersonalization, ConsentType.AD_PERSONALIZATION.toApple())
        assertEquals<Any?>(FIRConsentTypeAnalyticsStorage, ConsentType.ANALYTICS_STORAGE.toApple())
        assertEquals(
            ConsentType.entries.size,
            ConsentType.entries.map { it.toApple() }.toSet().size,
        )
    }

    @Test
    fun consentStatusesMapToTheirOwnAppleConstants() {
        assertEquals<Any?>(FIRConsentStatusGranted, ConsentStatus.GRANTED.toApple())
        assertEquals<Any?>(FIRConsentStatusDenied, ConsentStatus.DENIED.toApple())
    }
}
