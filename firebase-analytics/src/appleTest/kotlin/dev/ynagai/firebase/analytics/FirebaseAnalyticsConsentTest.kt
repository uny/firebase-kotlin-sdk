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

    // The dictionary is write-only from Kotlin, so pin it before it is handed
    // over. Inverting key and value, or dropping an entry, is accepted by
    // FIRAnalytics without crashing and silently loses every consent setting.
    @Test
    fun consentMapIsKeyedByTypeAndValuedByStatus() {
        val mapped = mapOf(
            ConsentType.AD_STORAGE to ConsentStatus.DENIED,
            ConsentType.ANALYTICS_STORAGE to ConsentStatus.GRANTED,
        ).toApple()

        assertEquals(2, mapped.size)
        assertEquals<Any?>(FIRConsentStatusDenied, mapped[FIRConsentTypeAdStorage])
        assertEquals<Any?>(FIRConsentStatusGranted, mapped[FIRConsentTypeAnalyticsStorage])
    }

    // A wrong constant collapses two keys into one, so setConsent drops a
    // consent type without crashing.
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
