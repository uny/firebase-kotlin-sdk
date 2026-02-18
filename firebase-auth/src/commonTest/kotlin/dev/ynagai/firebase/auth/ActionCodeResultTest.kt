package dev.ynagai.firebase.auth

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ActionCodeResultTest {

    @Test
    fun defaultValues() {
        val result = ActionCodeResult(operation = ActionCodeOperation.PASSWORD_RESET)
        assertEquals(ActionCodeOperation.PASSWORD_RESET, result.operation)
        assertNull(result.email)
        assertNull(result.previousEmail)
    }

    @Test
    fun allPropertiesSet() {
        val result = ActionCodeResult(
            operation = ActionCodeOperation.RECOVER_EMAIL,
            email = "new@example.com",
            previousEmail = "old@example.com",
        )
        assertEquals(ActionCodeOperation.RECOVER_EMAIL, result.operation)
        assertEquals("new@example.com", result.email)
        assertEquals("old@example.com", result.previousEmail)
    }

    @Test
    fun equalResultsAreEqual() {
        val r1 = ActionCodeResult(
            operation = ActionCodeOperation.VERIFY_EMAIL,
            email = "test@example.com",
        )
        val r2 = ActionCodeResult(
            operation = ActionCodeOperation.VERIFY_EMAIL,
            email = "test@example.com",
        )
        assertEquals(r1, r2)
    }

    @Test
    fun copyWorks() {
        val original = ActionCodeResult(operation = ActionCodeOperation.PASSWORD_RESET)
        val copy = original.copy(email = "user@example.com")
        assertEquals(ActionCodeOperation.PASSWORD_RESET, copy.operation)
        assertEquals("user@example.com", copy.email)
    }

    @Test
    fun allOperationsExist() {
        val expected = setOf(
            ActionCodeOperation.PASSWORD_RESET,
            ActionCodeOperation.VERIFY_EMAIL,
            ActionCodeOperation.RECOVER_EMAIL,
            ActionCodeOperation.SIGN_IN_WITH_EMAIL_LINK,
            ActionCodeOperation.VERIFY_AND_CHANGE_EMAIL,
            ActionCodeOperation.REVERT_SECOND_FACTOR_ADDITION,
            ActionCodeOperation.UNKNOWN,
        )
        assertEquals(expected, ActionCodeOperation.entries.toSet())
    }
}
