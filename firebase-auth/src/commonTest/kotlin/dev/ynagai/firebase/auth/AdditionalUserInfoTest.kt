package dev.ynagai.firebase.auth

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class AdditionalUserInfoTest {

    @Test
    fun nullProfileAndUsernameAreHandled() {
        val info = AdditionalUserInfo(
            providerId = "google.com",
            username = null,
            profile = null,
            isNewUser = true,
        )
        assertNull(info.username)
        assertNull(info.profile)
    }

    @Test
    fun allPropertiesAreAccessible() {
        val profile = mapOf("key" to "value", "nested" to null)
        val info = AdditionalUserInfo(
            providerId = "github.com",
            username = "testuser",
            profile = profile,
            isNewUser = false,
        )
        assertEquals("github.com", info.providerId)
        assertEquals("testuser", info.username)
        assertEquals(profile, info.profile)
        assertFalse(info.isNewUser)
    }

    @Test
    fun equalInfosAreEqual() {
        val i1 = AdditionalUserInfo(
            providerId = "google.com",
            username = null,
            profile = null,
            isNewUser = true,
        )
        val i2 = AdditionalUserInfo(
            providerId = "google.com",
            username = null,
            profile = null,
            isNewUser = true,
        )
        assertEquals(i1, i2)
    }

    @Test
    fun isNewUserFlag() {
        val newUser = AdditionalUserInfo(
            providerId = "password",
            username = null,
            profile = null,
            isNewUser = true,
        )
        assertTrue(newUser.isNewUser)

        val existingUser = AdditionalUserInfo(
            providerId = "password",
            username = null,
            profile = null,
            isNewUser = false,
        )
        assertFalse(existingUser.isNewUser)
    }
}
