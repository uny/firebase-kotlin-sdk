package dev.ynagai.firebase.ai

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class HttpStatusParserTest {

    @Test
    fun parsesExplicitHttpMarker() {
        assertEquals(500, parseHttpStatusFromMessage("HTTP 500 Internal Server Error"))
        assertEquals(503, parseHttpStatusFromMessage("Request failed: HTTP 503"))
        assertEquals(404, parseHttpStatusFromMessage("http/1.1 404 not found"))
    }

    @Test
    fun parsesStatusMarker() {
        assertEquals(500, parseHttpStatusFromMessage("Server returned status: 500"))
        assertEquals(429, parseHttpStatusFromMessage("status code 429 too many requests"))
    }

    @Test
    fun parsesCodeMarker() {
        assertEquals(500, parseHttpStatusFromMessage("error code: 500"))
        assertEquals(502, parseHttpStatusFromMessage("code=502"))
    }

    @Test
    fun parsesParenthesizedCode() {
        assertEquals(500, parseHttpStatusFromMessage("Internal Server Error (500)"))
    }

    @Test
    fun rejectsBareDigitsWithoutMarker() {
        // These would have matched the original `\b[45]\d{2}\b` regex.
        assertNull(parseHttpStatusFromMessage("Request exceeded 500 tokens"))
        assertNull(parseHttpStatusFromMessage("Listening on :4433"))
        assertNull(parseHttpStatusFromMessage("Processed 450 items successfully"))
    }

    @Test
    fun rejectsOutOfRangeNumbers() {
        assertNull(parseHttpStatusFromMessage("error code: 999"))
        assertNull(parseHttpStatusFromMessage("HTTP 200 OK"))
        assertNull(parseHttpStatusFromMessage("status: 100"))
    }

    @Test
    fun returnsNullForEmptyOrMissing() {
        assertNull(parseHttpStatusFromMessage(null))
        assertNull(parseHttpStatusFromMessage(""))
        assertNull(parseHttpStatusFromMessage("something went wrong"))
    }
}
