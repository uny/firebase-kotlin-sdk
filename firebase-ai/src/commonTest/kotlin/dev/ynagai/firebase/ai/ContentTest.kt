package dev.ynagai.firebase.ai

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class ContentTest {

    @Test
    fun inlineDataPartEqualityWithSameData() {
        val data = byteArrayOf(1, 2, 3)
        val a = InlineDataPart("image/png", data)
        val b = InlineDataPart("image/png", byteArrayOf(1, 2, 3))
        assertEquals(a, b)
        assertEquals(a.hashCode(), b.hashCode())
    }

    @Test
    fun inlineDataPartInequalityWithDifferentData() {
        val a = InlineDataPart("image/png", byteArrayOf(1, 2, 3))
        val b = InlineDataPart("image/png", byteArrayOf(4, 5, 6))
        assertNotEquals(a, b)
    }

    @Test
    fun inlineDataPartInequalityWithDifferentMimeType() {
        val data = byteArrayOf(1, 2, 3)
        val a = InlineDataPart("image/png", data)
        val b = InlineDataPart("image/jpeg", data)
        assertNotEquals(a, b)
    }

    @Test
    fun inlineDataPartNotEqualToNull() {
        val part = InlineDataPart("image/png", byteArrayOf(1))
        assertFalse(part.equals(null))
    }

    @Test
    fun inlineDataPartNotEqualToOtherType() {
        val part = InlineDataPart("image/png", byteArrayOf(1))
        assertFalse(part.equals("not a part"))
    }

    @Test
    fun inlineDataPartIdentityEquality() {
        val part = InlineDataPart("image/png", byteArrayOf(1))
        assertTrue(part.equals(part))
    }

    @Test
    fun fileDataPartDataClassEquality() {
        val a = FileDataPart("application/pdf", "gs://bucket/file.pdf")
        val b = FileDataPart("application/pdf", "gs://bucket/file.pdf")
        assertEquals(a, b)
        assertEquals(a.hashCode(), b.hashCode())
    }

    @Test
    fun fileDataPartInequality() {
        val a = FileDataPart("application/pdf", "gs://bucket/a.pdf")
        val b = FileDataPart("application/pdf", "gs://bucket/b.pdf")
        assertNotEquals(a, b)
    }

    @Test
    fun functionCallPartDataClassEquality() {
        val a = FunctionCallPart("getWeather", mapOf("city" to "Tokyo"))
        val b = FunctionCallPart("getWeather", mapOf("city" to "Tokyo"))
        assertEquals(a, b)
        assertEquals(a.hashCode(), b.hashCode())
    }

    @Test
    fun functionCallPartDefaultArgs() {
        val part = FunctionCallPart("noArgs")
        assertTrue(part.args.isEmpty())
    }

    @Test
    fun functionResponsePartDataClassEquality() {
        val a = FunctionResponsePart("getWeather", mapOf("temp" to 25))
        val b = FunctionResponsePart("getWeather", mapOf("temp" to 25))
        assertEquals(a, b)
        assertEquals(a.hashCode(), b.hashCode())
    }

    @Test
    fun functionResponsePartDefaultResponse() {
        val part = FunctionResponsePart("fn")
        assertTrue(part.response.isEmpty())
    }

    @Test
    fun contentDataClassEquality() {
        val a = Content(role = "user", parts = listOf(TextPart("hello")))
        val b = Content(role = "user", parts = listOf(TextPart("hello")))
        assertEquals(a, b)
        assertEquals(a.hashCode(), b.hashCode())
    }

    @Test
    fun contentDefaults() {
        val content = Content()
        assertEquals(null, content.role)
        assertTrue(content.parts.isEmpty())
    }

    @Test
    fun textPartDataClassEquality() {
        val a = TextPart("hello")
        val b = TextPart("hello")
        assertEquals(a, b)
        assertEquals(a.hashCode(), b.hashCode())
    }

    @Test
    fun executableCodePartDataClassEquality() {
        val a = ExecutableCodePart(language = "PYTHON", code = "print('hi')")
        val b = ExecutableCodePart(language = "PYTHON", code = "print('hi')")
        assertEquals(a, b)
        assertEquals(a.hashCode(), b.hashCode())
    }

    @Test
    fun executableCodePartInequality() {
        val a = ExecutableCodePart(language = "PYTHON", code = "print('a')")
        val b = ExecutableCodePart(language = "PYTHON", code = "print('b')")
        assertNotEquals(a, b)
    }

    @Test
    fun codeExecutionResultPartDataClassEquality() {
        val a = CodeExecutionResultPart(outcome = CodeExecutionOutcome.OK, output = "42")
        val b = CodeExecutionResultPart(outcome = CodeExecutionOutcome.OK, output = "42")
        assertEquals(a, b)
        assertEquals(a.hashCode(), b.hashCode())
    }

    @Test
    fun codeExecutionResultPartDefaultOutput() {
        val part = CodeExecutionResultPart(outcome = CodeExecutionOutcome.OK)
        assertEquals("", part.output)
    }

    @Test
    fun codeExecutionOutcomeValues() {
        assertEquals(4, CodeExecutionOutcome.entries.size)
        assertTrue(CodeExecutionOutcome.entries.contains(CodeExecutionOutcome.OK))
        assertTrue(CodeExecutionOutcome.entries.contains(CodeExecutionOutcome.FAILED))
        assertTrue(CodeExecutionOutcome.entries.contains(CodeExecutionOutcome.DEADLINE_EXCEEDED))
        assertTrue(CodeExecutionOutcome.entries.contains(CodeExecutionOutcome.UNSPECIFIED))
    }
}
