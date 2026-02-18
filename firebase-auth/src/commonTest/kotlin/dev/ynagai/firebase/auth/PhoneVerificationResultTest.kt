package dev.ynagai.firebase.auth

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotEquals

class PhoneVerificationResultTest {

    @Test
    fun codeSentHoldsVerificationId() {
        val result = PhoneVerificationResult.CodeSent("vid-123")
        assertIs<PhoneVerificationResult>(result)
        assertEquals("vid-123", result.verificationId)
    }

    @Test
    fun codeSentEquality() {
        val a = PhoneVerificationResult.CodeSent("vid-1")
        val b = PhoneVerificationResult.CodeSent("vid-1")
        val c = PhoneVerificationResult.CodeSent("vid-2")
        assertEquals(a, b)
        assertNotEquals(a, c)
    }

    @Test
    fun codeSentCopy() {
        val original = PhoneVerificationResult.CodeSent("vid-1")
        val copy = original.copy(verificationId = "vid-2")
        assertEquals("vid-2", copy.verificationId)
        assertNotEquals(original, copy)
    }
}
