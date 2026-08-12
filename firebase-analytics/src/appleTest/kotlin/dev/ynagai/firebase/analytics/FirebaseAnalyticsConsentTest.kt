package dev.ynagai.firebase.analytics

import kotlin.test.Test

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
}
