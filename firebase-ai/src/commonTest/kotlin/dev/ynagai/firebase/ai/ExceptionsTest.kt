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
        assertNull(ex.httpStatusCode)
        assertNull(ex.responseBody)
        assertNull(ex.errorType)
        assertNull(ex.underlyingDomain)
        assertNull(ex.underlyingCode)
    }

    @Test
    fun firebaseAIExceptionCarriesHttpDetails() {
        val ex = FirebaseAIException(
            message = "boom",
            httpStatusCode = 500,
            responseBody = "{\"error\":\"oops\"}",
            errorType = FirebaseAIErrorType.INTERNAL,
            underlyingDomain = "com.google.firebase.firebaseai.BackendError",
            underlyingCode = 500,
        )
        assertEquals(500, ex.httpStatusCode)
        assertEquals("{\"error\":\"oops\"}", ex.responseBody)
        assertEquals(FirebaseAIErrorType.INTERNAL, ex.errorType)
        assertEquals("com.google.firebase.firebaseai.BackendError", ex.underlyingDomain)
        assertEquals(500, ex.underlyingCode)
    }

    @Test
    fun serverExceptionExposesHttpDetails() {
        val ex = ServerException(
            message = "Internal Server Error",
            httpStatusCode = 500,
            responseBody = "backend overloaded",
        )
        assertEquals(500, ex.httpStatusCode)
        assertEquals("backend overloaded", ex.responseBody)
        assertEquals(FirebaseAIErrorType.SERVER, ex.errorType)
    }

    @Test
    fun responseStoppedExceptionCarriesFinishReason() {
        val ex = ResponseStoppedException(message = "stopped", finishReason = "SAFETY")
        assertEquals("SAFETY", ex.finishReason)
        assertEquals(FirebaseAIErrorType.RESPONSE_STOPPED_EARLY, ex.errorType)
    }

    @Test
    fun subclassesHaveErrorTypes() {
        assertEquals(FirebaseAIErrorType.PROMPT_BLOCKED, PromptBlockedException("x").errorType)
        assertEquals(FirebaseAIErrorType.INVALID_API_KEY, InvalidAPIKeyException("x").errorType)
        assertEquals(FirebaseAIErrorType.QUOTA_EXCEEDED, QuotaExceededException("x").errorType)
    }

    @Test
    fun subclassesForwardNativeMetadata() {
        val server = ServerException(
            message = "boom",
            httpStatusCode = 503,
            responseBody = "unavailable",
            underlyingDomain = "FIRFirebaseAIErrorDomain",
            underlyingCode = -12,
        )
        assertEquals(503, server.httpStatusCode)
        assertEquals("unavailable", server.responseBody)
        assertEquals("FIRFirebaseAIErrorDomain", server.underlyingDomain)
        assertEquals(-12, server.underlyingCode)

        val invalid = InvalidAPIKeyException(
            message = "bad key",
            httpStatusCode = 403,
            underlyingDomain = "FIRFirebaseAIErrorDomain",
            underlyingCode = 17,
        )
        assertEquals(403, invalid.httpStatusCode)
        assertEquals("FIRFirebaseAIErrorDomain", invalid.underlyingDomain)
        assertEquals(17, invalid.underlyingCode)

        val quota = QuotaExceededException(
            message = "too many",
            httpStatusCode = 429,
            underlyingCode = 8,
        )
        assertEquals(429, quota.httpStatusCode)
        assertEquals(8, quota.underlyingCode)

        val blocked = PromptBlockedException(
            message = "blocked",
            underlyingDomain = "FIRFirebaseAIErrorDomain",
            underlyingCode = 3,
        )
        assertEquals("FIRFirebaseAIErrorDomain", blocked.underlyingDomain)
        assertEquals(3, blocked.underlyingCode)
    }
}
