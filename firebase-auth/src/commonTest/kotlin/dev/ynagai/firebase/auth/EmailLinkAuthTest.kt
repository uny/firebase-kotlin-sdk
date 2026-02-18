package dev.ynagai.firebase.auth

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class EmailLinkAuthTest {

    @Test
    fun actionCodeSettingsForEmailLink() {
        val settings = ActionCodeSettings(
            url = "https://example.com/finishSignUp",
            handleCodeInApp = true,
            androidPackageName = "com.example.app",
            androidInstallIfNotAvailable = true,
            androidMinimumVersion = "21",
            iOSBundleId = "com.example.app.ios",
            dynamicLinkDomain = "example.page.link",
        )
        assertEquals("https://example.com/finishSignUp", settings.url)
        assertTrue(settings.handleCodeInApp)
        assertEquals("com.example.app", settings.androidPackageName)
        assertTrue(settings.androidInstallIfNotAvailable)
        assertEquals("21", settings.androidMinimumVersion)
        assertEquals("com.example.app.ios", settings.iOSBundleId)
        assertEquals("example.page.link", settings.dynamicLinkDomain)
    }

    @Test
    fun actionCodeSettingsMinimalForEmailLink() {
        val settings = ActionCodeSettings(
            url = "https://example.com/finishSignUp",
            handleCodeInApp = true,
        )
        assertEquals("https://example.com/finishSignUp", settings.url)
        assertTrue(settings.handleCodeInApp)
    }
}
