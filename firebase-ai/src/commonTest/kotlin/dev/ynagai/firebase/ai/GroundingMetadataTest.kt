package dev.ynagai.firebase.ai

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class GroundingMetadataTest {

    @Test
    fun defaultValues() {
        val metadata = GroundingMetadata()
        assertTrue(metadata.webSearchQueries.isEmpty())
        assertNull(metadata.searchEntryPoint)
        assertTrue(metadata.groundingChunks.isEmpty())
        assertTrue(metadata.groundingSupports.isEmpty())
    }

    @Test
    fun withWebSearchQueries() {
        val metadata = GroundingMetadata(
            webSearchQueries = listOf("kotlin multiplatform", "firebase sdk"),
        )
        assertEquals(2, metadata.webSearchQueries.size)
        assertEquals("kotlin multiplatform", metadata.webSearchQueries[0])
    }

    @Test
    fun searchEntryPoint() {
        val entry = SearchEntryPoint(renderedContent = "<div>results</div>")
        assertEquals("<div>results</div>", entry.renderedContent)
    }

    @Test
    fun webGroundingChunkDefaultsToNull() {
        val chunk = WebGroundingChunk()
        assertNull(chunk.uri)
        assertNull(chunk.title)
        assertNull(chunk.domain)
    }

    @Test
    fun groundingChunkWithWebSource() {
        val chunk = GroundingChunk(
            web = WebGroundingChunk(
                uri = "https://example.com",
                title = "Example",
                domain = "example.com",
            ),
        )
        assertEquals("https://example.com", chunk.web?.uri)
        assertEquals("Example", chunk.web?.title)
        assertEquals("example.com", chunk.web?.domain)
    }

    @Test
    fun segmentDefaultValues() {
        val segment = Segment()
        assertEquals(0, segment.partIndex)
        assertEquals(0, segment.startIndex)
        assertEquals(0, segment.endIndex)
        assertEquals("", segment.text)
    }

    @Test
    fun groundingSupportWithIndices() {
        val support = GroundingSupport(
            segment = Segment(partIndex = 0, startIndex = 10, endIndex = 50, text = "some text"),
            groundingChunkIndices = listOf(0, 2),
        )
        assertEquals("some text", support.segment.text)
        assertEquals(listOf(0, 2), support.groundingChunkIndices)
    }

    @Test
    fun dataClassEquality() {
        val a = GroundingMetadata(webSearchQueries = listOf("test"))
        val b = GroundingMetadata(webSearchQueries = listOf("test"))
        assertEquals(a, b)
        assertEquals(a.hashCode(), b.hashCode())
    }
}
