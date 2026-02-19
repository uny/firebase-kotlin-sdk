package dev.ynagai.firebase.auth

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertNotEquals

class MultiFactorInfoTest {

    @Test
    fun phoneMultiFactorInfoProperties() {
        val info = PhoneMultiFactorInfo(
            uid = "factor-1",
            displayName = "My Phone",
            factorId = "phone",
            enrollmentTimestamp = 1700000000000L,
            phoneNumber = "+1234567890",
        )
        assertEquals("factor-1", info.uid)
        assertEquals("My Phone", info.displayName)
        assertEquals("phone", info.factorId)
        assertEquals(1700000000000L, info.enrollmentTimestamp)
        assertEquals("+1234567890", info.phoneNumber)
    }

    @Test
    fun totpMultiFactorInfoProperties() {
        val info = TotpMultiFactorInfo(
            uid = "factor-2",
            displayName = "My TOTP",
            factorId = "totp",
            enrollmentTimestamp = 1700000000000L,
        )
        assertEquals("factor-2", info.uid)
        assertEquals("My TOTP", info.displayName)
        assertEquals("totp", info.factorId)
        assertEquals(1700000000000L, info.enrollmentTimestamp)
    }

    @Test
    fun phoneMultiFactorInfoIsMultiFactorInfo() {
        val info: MultiFactorInfo = PhoneMultiFactorInfo(
            uid = "factor-1",
            displayName = null,
            factorId = "phone",
            enrollmentTimestamp = 0L,
            phoneNumber = "+1234567890",
        )
        assertIs<PhoneMultiFactorInfo>(info)
        assertIs<MultiFactorInfo>(info)
    }

    @Test
    fun totpMultiFactorInfoIsMultiFactorInfo() {
        val info: MultiFactorInfo = TotpMultiFactorInfo(
            uid = "factor-2",
            displayName = null,
            factorId = "totp",
            enrollmentTimestamp = 0L,
        )
        assertIs<TotpMultiFactorInfo>(info)
        assertIs<MultiFactorInfo>(info)
    }

    @Test
    fun nullDisplayName() {
        val phone = PhoneMultiFactorInfo(
            uid = "f1",
            displayName = null,
            factorId = "phone",
            enrollmentTimestamp = 0L,
            phoneNumber = "+1",
        )
        assertNull(phone.displayName)

        val totp = TotpMultiFactorInfo(
            uid = "f2",
            displayName = null,
            factorId = "totp",
            enrollmentTimestamp = 0L,
        )
        assertNull(totp.displayName)
    }

    @Test
    fun phoneMultiFactorInfoEquality() {
        val a = PhoneMultiFactorInfo("f1", "name", "phone", 100L, "+1")
        val b = PhoneMultiFactorInfo("f1", "name", "phone", 100L, "+1")
        val c = PhoneMultiFactorInfo("f2", "name", "phone", 100L, "+1")
        assertEquals(a, b)
        assertEquals(a.hashCode(), b.hashCode())
        assertNotEquals(a, c)
    }

    @Test
    fun totpMultiFactorInfoEquality() {
        val a = TotpMultiFactorInfo("f1", "name", "totp", 100L)
        val b = TotpMultiFactorInfo("f1", "name", "totp", 100L)
        val c = TotpMultiFactorInfo("f2", "name", "totp", 100L)
        assertEquals(a, b)
        assertEquals(a.hashCode(), b.hashCode())
        assertNotEquals(a, c)
    }

    @Test
    fun sealedClassPatternMatching() {
        val factors: List<MultiFactorInfo> = listOf(
            PhoneMultiFactorInfo("f1", null, "phone", 0L, "+1"),
            TotpMultiFactorInfo("f2", null, "totp", 0L),
        )
        val results = factors.map { info ->
            when (info) {
                is PhoneMultiFactorInfo -> "phone:${info.phoneNumber}"
                is TotpMultiFactorInfo -> "totp:${info.uid}"
            }
        }
        assertEquals(listOf("phone:+1", "totp:f2"), results)
    }

    @Test
    fun copyPhoneMultiFactorInfo() {
        val original = PhoneMultiFactorInfo("f1", "name", "phone", 100L, "+1")
        val copy = original.copy(displayName = "new name")
        assertEquals("new name", copy.displayName)
        assertEquals(original.uid, copy.uid)
        assertEquals(original.phoneNumber, copy.phoneNumber)
    }

    @Test
    fun copyTotpMultiFactorInfo() {
        val original = TotpMultiFactorInfo("f1", "name", "totp", 100L)
        val copy = original.copy(displayName = "new name")
        assertEquals("new name", copy.displayName)
        assertEquals(original.uid, copy.uid)
    }
}
