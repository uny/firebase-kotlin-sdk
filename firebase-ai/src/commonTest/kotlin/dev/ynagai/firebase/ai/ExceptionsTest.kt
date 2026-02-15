package dev.ynagai.firebase.ai

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull

class ExceptionsTest {

    @Test
    fun generativeAIExceptionIsFirebaseAIException() {
        val ex = GenerativeAIException("test error")
        assertIs<FirebaseAIException>(ex)
        assertEquals("test error", ex.message)
    }

    @Test
    fun generativeAIExceptionWithCause() {
        val cause = RuntimeException("root cause")
        val ex = GenerativeAIException("wrapped", cause)
        assertEquals(cause, ex.cause)
    }

    @Test
    fun promptBlockedExceptionIsFirebaseAIException() {
        val ex = PromptBlockedException("blocked")
        assertIs<FirebaseAIException>(ex)
        assertEquals("blocked", ex.message)
        assertNull(ex.response)
    }

    @Test
    fun promptBlockedExceptionWithResponse() {
        val response = GenerateContentResponse(candidates = emptyList())
        val ex = PromptBlockedException("blocked", response)
        assertEquals(response, ex.response)
    }

    @Test
    fun responseStoppedExceptionIsFirebaseAIException() {
        val ex = ResponseStoppedException("stopped")
        assertIs<FirebaseAIException>(ex)
        assertEquals("stopped", ex.message)
    }

    @Test
    fun responseStoppedExceptionWithResponse() {
        val response = GenerateContentResponse(candidates = emptyList())
        val ex = ResponseStoppedException("stopped", response)
        assertEquals(response, ex.response)
    }

    @Test
    fun invalidAPIKeyExceptionIsFirebaseAIException() {
        val ex = InvalidAPIKeyException("bad key")
        assertIs<FirebaseAIException>(ex)
        assertEquals("bad key", ex.message)
    }

    @Test
    fun quotaExceededExceptionIsFirebaseAIException() {
        val ex = QuotaExceededException("quota hit")
        assertIs<FirebaseAIException>(ex)
        assertEquals("quota hit", ex.message)
    }

    @Test
    fun serverExceptionIsFirebaseAIException() {
        val ex = ServerException("server error")
        assertIs<FirebaseAIException>(ex)
        assertEquals("server error", ex.message)
    }

    @Test
    fun serverExceptionWithCause() {
        val cause = RuntimeException("io")
        val ex = ServerException("server error", cause)
        assertEquals(cause, ex.cause)
    }

    @Test
    fun firebaseAIExceptionDefaultsToNull() {
        val ex = FirebaseAIException()
        assertNull(ex.message)
        assertNull(ex.cause)
    }
}
