package dev.ynagai.firebase.ai

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class ContentBuilderTest {

    @Test
    fun textPartIsAdded() {
        val result = content {
            text("Hello")
        }
        assertEquals(1, result.parts.size)
        assertIs<TextPart>(result.parts[0])
        assertEquals("Hello", (result.parts[0] as TextPart).text)
    }

    @Test
    fun defaultRoleIsUser() {
        val result = content {
            text("Hello")
        }
        assertEquals("user", result.role)
    }

    @Test
    fun customRoleIsSet() {
        val result = content(role = "model") {
            text("Hello")
        }
        assertEquals("model", result.role)
    }

    @Test
    fun multiplePartsAreAdded() {
        val result = content {
            text("Hello")
            text("World")
        }
        assertEquals(2, result.parts.size)
    }

    @Test
    fun inlineDataPartIsAdded() {
        val data = byteArrayOf(1, 2, 3)
        val result = content {
            inlineData("image/png", data)
        }
        assertEquals(1, result.parts.size)
        assertIs<InlineDataPart>(result.parts[0])
        val part = result.parts[0] as InlineDataPart
        assertEquals("image/png", part.mimeType)
        assertTrue(data.contentEquals(part.data))
    }

    @Test
    fun functionCallPartIsAdded() {
        val result = content {
            functionCall("getWeather", mapOf("city" to "Tokyo"))
        }
        assertEquals(1, result.parts.size)
        assertIs<FunctionCallPart>(result.parts[0])
        val part = result.parts[0] as FunctionCallPart
        assertEquals("getWeather", part.name)
        assertEquals(mapOf("city" to "Tokyo"), part.args)
    }

    @Test
    fun functionResponsePartIsAdded() {
        val result = content(role = "function") {
            functionResponse("getWeather", mapOf("temp" to 25))
        }
        assertEquals(1, result.parts.size)
        assertIs<FunctionResponsePart>(result.parts[0])
        val part = result.parts[0] as FunctionResponsePart
        assertEquals("getWeather", part.name)
        assertEquals(mapOf<String, Any?>("temp" to 25), part.response)
    }

    @Test
    fun mixedPartsAreAdded() {
        val result = content {
            text("Look at this:")
            inlineData("image/jpeg", byteArrayOf(0))
            text("What do you think?")
        }
        assertEquals(3, result.parts.size)
        assertIs<TextPart>(result.parts[0])
        assertIs<InlineDataPart>(result.parts[1])
        assertIs<TextPart>(result.parts[2])
    }

    @Test
    fun fileDataPartIsAdded() {
        val result = content {
            fileData("application/pdf", "gs://bucket/file.pdf")
        }
        assertEquals(1, result.parts.size)
        assertIs<FileDataPart>(result.parts[0])
        val part = result.parts[0] as FileDataPart
        assertEquals("application/pdf", part.mimeType)
        assertEquals("gs://bucket/file.pdf", part.uri)
    }

    @Test
    fun emptyContentHasNoParts() {
        val result = content { }
        assertTrue(result.parts.isEmpty())
    }
}
