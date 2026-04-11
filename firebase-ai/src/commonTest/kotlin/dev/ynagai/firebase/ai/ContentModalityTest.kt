package dev.ynagai.firebase.ai

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ContentModalityTest {

    @Test
    fun contentModalityValues() {
        assertEquals(6, ContentModality.entries.size)
        assertTrue(ContentModality.entries.contains(ContentModality.TEXT))
        assertTrue(ContentModality.entries.contains(ContentModality.IMAGE))
        assertTrue(ContentModality.entries.contains(ContentModality.AUDIO))
        assertTrue(ContentModality.entries.contains(ContentModality.VIDEO))
        assertTrue(ContentModality.entries.contains(ContentModality.DOCUMENT))
        assertTrue(ContentModality.entries.contains(ContentModality.UNSPECIFIED))
    }

    @Test
    fun modalityTokenCountEquality() {
        val a = ModalityTokenCount(ContentModality.TEXT, 100)
        val b = ModalityTokenCount(ContentModality.TEXT, 100)
        assertEquals(a, b)
        assertEquals(a.hashCode(), b.hashCode())
    }

    @Test
    fun modalityTokenCountProperties() {
        val count = ModalityTokenCount(ContentModality.IMAGE, 256)
        assertEquals(ContentModality.IMAGE, count.modality)
        assertEquals(256, count.tokenCount)
    }
}
