package dev.ynagai.firebase.auth

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ActionCodeSettingsTest {

    @Test
    fun defaultValues() {
        val settings = ActionCodeSettings(url = "https://example.com")
        assertEquals("https://example.com", settings.url)
        assertFalse(settings.handleCodeInApp)
        assertNull(settings.androidPackageName)
        assertFalse(settings.androidInstallIfNotAvailable)
        assertNull(settings.androidMinimumVersion)
        assertNull(settings.iOSBundleId)
        assertNull(settings.dynamicLinkDomain)
    }

    @Test
    fun allPropertiesSet() {
        val settings = ActionCodeSettings(
            url = "https://example.com/action",
            handleCodeInApp = true,
            androidPackageName = "com.example.app",
            androidInstallIfNotAvailable = true,
            androidMinimumVersion = "21",
            iOSBundleId = "com.example.app.ios",
            dynamicLinkDomain = "example.page.link",
        )
        assertEquals("https://example.com/action", settings.url)
        assertTrue(settings.handleCodeInApp)
        assertEquals("com.example.app", settings.androidPackageName)
        assertTrue(settings.androidInstallIfNotAvailable)
        assertEquals("21", settings.androidMinimumVersion)
        assertEquals("com.example.app.ios", settings.iOSBundleId)
        assertEquals("example.page.link", settings.dynamicLinkDomain)
    }

    @Test
    fun equalSettingsAreEqual() {
        val s1 = ActionCodeSettings(url = "https://example.com", handleCodeInApp = true)
        val s2 = ActionCodeSettings(url = "https://example.com", handleCodeInApp = true)
        assertEquals(s1, s2)
    }

    @Test
    fun copyWorks() {
        val original = ActionCodeSettings(url = "https://example.com")
        val copy = original.copy(handleCodeInApp = true)
        assertTrue(copy.handleCodeInApp)
        assertEquals("https://example.com", copy.url)
    }
}
