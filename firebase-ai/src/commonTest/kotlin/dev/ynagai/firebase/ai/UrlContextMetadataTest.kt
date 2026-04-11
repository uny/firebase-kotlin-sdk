package dev.ynagai.firebase.ai

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class UrlContextMetadataTest {

    @Test
    fun defaultValues() {
        val metadata = UrlContextMetadata()
        assertTrue(metadata.urlMetadata.isEmpty())
    }

    @Test
    fun urlMetadataDefaultValues() {
        val meta = UrlMetadata()
        assertNull(meta.retrievedUrl)
        assertEquals(UrlRetrievalStatus.UNSPECIFIED, meta.retrievalStatus)
    }

    @Test
    fun urlMetadataWithValues() {
        val meta = UrlMetadata(
            retrievedUrl = "https://example.com/page",
            retrievalStatus = UrlRetrievalStatus.SUCCESS,
        )
        assertEquals("https://example.com/page", meta.retrievedUrl)
        assertEquals(UrlRetrievalStatus.SUCCESS, meta.retrievalStatus)
    }

    @Test
    fun urlRetrievalStatusValues() {
        assertEquals(5, UrlRetrievalStatus.entries.size)
        assertTrue(UrlRetrievalStatus.entries.contains(UrlRetrievalStatus.SUCCESS))
        assertTrue(UrlRetrievalStatus.entries.contains(UrlRetrievalStatus.ERROR))
        assertTrue(UrlRetrievalStatus.entries.contains(UrlRetrievalStatus.PAYWALL))
        assertTrue(UrlRetrievalStatus.entries.contains(UrlRetrievalStatus.UNSAFE))
        assertTrue(UrlRetrievalStatus.entries.contains(UrlRetrievalStatus.UNSPECIFIED))
    }

    @Test
    fun urlContextMetadataWithEntries() {
        val metadata = UrlContextMetadata(
            urlMetadata = listOf(
                UrlMetadata("https://a.com", UrlRetrievalStatus.SUCCESS),
                UrlMetadata("https://b.com", UrlRetrievalStatus.PAYWALL),
            ),
        )
        assertEquals(2, metadata.urlMetadata.size)
        assertEquals(UrlRetrievalStatus.PAYWALL, metadata.urlMetadata[1].retrievalStatus)
    }

    @Test
    fun dataClassEquality() {
        val a = UrlMetadata("https://a.com", UrlRetrievalStatus.SUCCESS)
        val b = UrlMetadata("https://a.com", UrlRetrievalStatus.SUCCESS)
        assertEquals(a, b)
        assertEquals(a.hashCode(), b.hashCode())
    }
}
