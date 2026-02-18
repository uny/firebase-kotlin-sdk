package dev.ynagai.firebase.auth

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNull

class UserInfoTest {

    @Test
    fun equalUserInfosAreEqual() {
        val u1 = UserInfo(
            uid = "uid1",
            providerId = "google.com",
            displayName = "Test User",
            email = "test@example.com",
            phoneNumber = "+1234567890",
            photoUrl = "https://example.com/photo.jpg",
        )
        val u2 = UserInfo(
            uid = "uid1",
            providerId = "google.com",
            displayName = "Test User",
            email = "test@example.com",
            phoneNumber = "+1234567890",
            photoUrl = "https://example.com/photo.jpg",
        )
        assertEquals(u1, u2)
    }

    @Test
    fun differentUserInfosAreNotEqual() {
        val u1 = UserInfo(
            uid = "uid1",
            providerId = "google.com",
            displayName = "Test User",
            email = "test@example.com",
            phoneNumber = null,
            photoUrl = null,
        )
        val u2 = UserInfo(
            uid = "uid2",
            providerId = "google.com",
            displayName = "Other User",
            email = "other@example.com",
            phoneNumber = null,
            photoUrl = null,
        )
        assertNotEquals(u1, u2)
    }

    @Test
    fun copyWorks() {
        val original = UserInfo(
            uid = "uid1",
            providerId = "google.com",
            displayName = "Test User",
            email = "test@example.com",
            phoneNumber = null,
            photoUrl = null,
        )
        val copy = original.copy(email = "new@example.com")
        assertEquals("new@example.com", copy.email)
        assertEquals("uid1", copy.uid)
    }

    @Test
    fun propertiesAreAccessible() {
        val info = UserInfo(
            uid = "uid1",
            providerId = "password",
            displayName = null,
            email = "test@example.com",
            phoneNumber = null,
            photoUrl = null,
        )
        assertEquals("uid1", info.uid)
        assertEquals("password", info.providerId)
        assertNull(info.displayName)
        assertEquals("test@example.com", info.email)
        assertNull(info.phoneNumber)
        assertNull(info.photoUrl)
    }

    @Test
    fun hashCodeIsConsistent() {
        val u1 = UserInfo(
            uid = "uid1",
            providerId = "google.com",
            displayName = "Test",
            email = "test@example.com",
            phoneNumber = null,
            photoUrl = null,
        )
        val u2 = UserInfo(
            uid = "uid1",
            providerId = "google.com",
            displayName = "Test",
            email = "test@example.com",
            phoneNumber = null,
            photoUrl = null,
        )
        assertEquals(u1.hashCode(), u2.hashCode())
    }
}
