package dev.ynagai.firebase.auth

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class FirebaseAuthExceptionTest {

    @Test
    fun exceptionHasCorrectMessageAndCode() {
        val exception = FirebaseAuthException("Invalid email", FirebaseAuthExceptionCode.INVALID_EMAIL)
        assertEquals("Invalid email", exception.message)
        assertEquals(FirebaseAuthExceptionCode.INVALID_EMAIL, exception.errorCode)
    }

    @Test
    fun exceptionIsThrowable() {
        val exception = FirebaseAuthException("error", FirebaseAuthExceptionCode.UNKNOWN)
        assertIs<Exception>(exception)
    }

    @Test
    fun allEnumValuesExist() {
        val values = FirebaseAuthExceptionCode.entries
        assertTrue(values.size >= 42)
        assertTrue(values.contains(FirebaseAuthExceptionCode.INVALID_EMAIL))
        assertTrue(values.contains(FirebaseAuthExceptionCode.USER_NOT_FOUND))
        assertTrue(values.contains(FirebaseAuthExceptionCode.WRONG_PASSWORD))
        assertTrue(values.contains(FirebaseAuthExceptionCode.EMAIL_ALREADY_IN_USE))
        assertTrue(values.contains(FirebaseAuthExceptionCode.INVALID_PHONE_NUMBER))
        assertTrue(values.contains(FirebaseAuthExceptionCode.MISSING_PHONE_NUMBER))
        assertTrue(values.contains(FirebaseAuthExceptionCode.QUOTA_EXCEEDED))
        assertTrue(values.contains(FirebaseAuthExceptionCode.SESSION_EXPIRED))
        assertTrue(values.contains(FirebaseAuthExceptionCode.INVALID_VERIFICATION_CODE))
        assertTrue(values.contains(FirebaseAuthExceptionCode.INVALID_VERIFICATION_ID))
        assertTrue(values.contains(FirebaseAuthExceptionCode.INVALID_CUSTOM_TOKEN))
        assertTrue(values.contains(FirebaseAuthExceptionCode.CUSTOM_TOKEN_MISMATCH))
        assertTrue(values.contains(FirebaseAuthExceptionCode.MISSING_EMAIL))
        assertTrue(values.contains(FirebaseAuthExceptionCode.INVALID_API_KEY))
        assertTrue(values.contains(FirebaseAuthExceptionCode.INTERNAL_ERROR))
        assertTrue(values.contains(FirebaseAuthExceptionCode.REJECTED_CREDENTIAL))
        assertTrue(values.contains(FirebaseAuthExceptionCode.UNKNOWN))
    }

    @Test
    fun nullMessage() {
        val exception = FirebaseAuthException(null, FirebaseAuthExceptionCode.UNKNOWN)
        assertEquals(null, exception.message)
        assertEquals(FirebaseAuthExceptionCode.UNKNOWN, exception.errorCode)
    }
}
