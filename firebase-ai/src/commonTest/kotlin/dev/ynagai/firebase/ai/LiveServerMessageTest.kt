package dev.ynagai.firebase.ai

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertTrue

class LiveServerMessageTest {

    @Test
    fun contentMessageWithText() {
        val message = LiveServerMessage.Content(
            content = Content(role = "model", parts = listOf(TextPart("Hello"))),
            isTurnComplete = false,
            wasInterrupted = false,
        )
        assertIs<LiveServerMessage>(message)
        assertEquals("Hello", (message.content?.parts?.first() as TextPart).text)
        assertEquals(false, message.isTurnComplete)
        assertEquals(false, message.wasInterrupted)
    }

    @Test
    fun contentMessageTurnComplete() {
        val message = LiveServerMessage.Content(
            content = null,
            isTurnComplete = true,
            wasInterrupted = false,
        )
        assertNull(message.content)
        assertTrue(message.isTurnComplete)
    }

    @Test
    fun contentMessageInterrupted() {
        val message = LiveServerMessage.Content(
            content = null,
            isTurnComplete = false,
            wasInterrupted = true,
        )
        assertTrue(message.wasInterrupted)
    }

    @Test
    fun contentMessageDefaultValues() {
        val message = LiveServerMessage.Content(content = null)
        assertNull(message.content)
        assertEquals(false, message.isTurnComplete)
        assertEquals(false, message.wasInterrupted)
    }

    @Test
    fun toolCallMessage() {
        val functionCalls = listOf(
            FunctionCallPart("getWeather", mapOf("city" to "Tokyo")),
            FunctionCallPart("getTime", mapOf("timezone" to "JST")),
        )
        val message = LiveServerMessage.ToolCall(functionCalls = functionCalls)
        assertIs<LiveServerMessage>(message)
        assertEquals(2, message.functionCalls.size)
        assertEquals("getWeather", message.functionCalls[0].name)
        assertEquals("Tokyo", message.functionCalls[0].args["city"])
    }

    @Test
    fun toolCallCancellationMessage() {
        val message = LiveServerMessage.ToolCallCancellation(ids = listOf("call-1", "call-2"))
        assertIs<LiveServerMessage>(message)
        assertEquals(listOf("call-1", "call-2"), message.ids)
    }

    @Test
    fun goingAwayMessage() {
        val message: LiveServerMessage = LiveServerMessage.GoingAway
        assertIs<LiveServerMessage.GoingAway>(message)
    }

    @Test
    fun contentEquality() {
        val a = LiveServerMessage.Content(
            content = Content(role = "model", parts = listOf(TextPart("Hi"))),
            isTurnComplete = true,
        )
        val b = LiveServerMessage.Content(
            content = Content(role = "model", parts = listOf(TextPart("Hi"))),
            isTurnComplete = true,
        )
        assertEquals(a, b)
    }

    @Test
    fun toolCallEquality() {
        val a = LiveServerMessage.ToolCall(
            functionCalls = listOf(FunctionCallPart("fn", mapOf("k" to "v"))),
        )
        val b = LiveServerMessage.ToolCall(
            functionCalls = listOf(FunctionCallPart("fn", mapOf("k" to "v"))),
        )
        assertEquals(a, b)
    }

    @Test
    fun toolCallCancellationEquality() {
        val a = LiveServerMessage.ToolCallCancellation(ids = listOf("id1"))
        val b = LiveServerMessage.ToolCallCancellation(ids = listOf("id1"))
        assertEquals(a, b)
    }
}
