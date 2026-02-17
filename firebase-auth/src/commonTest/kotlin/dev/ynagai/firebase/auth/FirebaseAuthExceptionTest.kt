package dev.ynagai.firebase.auth

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class FirebaseAuthExceptionTest {

    @Test
    fun exceptionHasCorrectMessageAndCode() {
        val exception = FirebaseAuthException("Invalid email", AuthExceptionCode.INVALID_EMAIL)
        assertEquals("Invalid email", exception.message)
        assertEquals(AuthExceptionCode.INVALID_EMAIL, exception.code)
    }

    @Test
    fun exceptionIsThrowable() {
        val exception = FirebaseAuthException("error", AuthExceptionCode.UNKNOWN)
        assertIs<Exception>(exception)
    }

    @Test
    fun allEnumValuesExist() {
        val values = AuthExceptionCode.entries
        assertTrue(values.size >= 20)
        assertTrue(values.contains(AuthExceptionCode.INVALID_EMAIL))
        assertTrue(values.contains(AuthExceptionCode.USER_NOT_FOUND))
        assertTrue(values.contains(AuthExceptionCode.WRONG_PASSWORD))
        assertTrue(values.contains(AuthExceptionCode.EMAIL_ALREADY_IN_USE))
        assertTrue(values.contains(AuthExceptionCode.UNKNOWN))
    }

    @Test
    fun nullMessage() {
        val exception = FirebaseAuthException(null, AuthExceptionCode.UNKNOWN)
        assertEquals(null, exception.message)
        assertEquals(AuthExceptionCode.UNKNOWN, exception.code)
    }
}
