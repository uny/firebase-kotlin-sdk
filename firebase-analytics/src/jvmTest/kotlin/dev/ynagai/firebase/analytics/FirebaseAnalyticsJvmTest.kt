package dev.ynagai.firebase.analytics

import dev.ynagai.firebase.Firebase
import dev.ynagai.firebase.FirebaseOptions
import dev.ynagai.firebase.initialize
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class FirebaseAnalyticsJvmTest {

    @Test
    fun defaultInstanceRequiresInitializedDefaultApp() {
        assertFailsWith<IllegalStateException> { Firebase.analytics }
    }

    @Test
    fun callsAreAcceptedAndReturnNothing() = runTest {
        val app = Firebase.initialize(
            options = FirebaseOptions(apiKey = "fake-api-key", applicationId = "fake-app-id"),
            name = "jvm-test-analytics",
        )
        try {
            val analytics = Firebase.analytics(app)
            analytics.logEvent("test_event", mapOf("key" to "value"))
            analytics.setConsent(mapOf(ConsentType.ANALYTICS_STORAGE to ConsentStatus.GRANTED))
            assertNull(analytics.getAppInstanceId())
            assertNull(analytics.getSessionId())
        } finally {
            app.delete()
        }
    }
}
