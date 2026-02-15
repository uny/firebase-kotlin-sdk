package dev.ynagai.firebase.ai

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ImagenResponseTest {

    @Test
    fun inlineImageEqualityWithSameData() {
        val a = ImagenInlineImage(byteArrayOf(1, 2, 3), "image/png")
        val b = ImagenInlineImage(byteArrayOf(1, 2, 3), "image/png")
        assertEquals(a, b)
        assertEquals(a.hashCode(), b.hashCode())
    }

    @Test
    fun inlineImageInequalityWithDifferentData() {
        val a = ImagenInlineImage(byteArrayOf(1, 2, 3), "image/png")
        val b = ImagenInlineImage(byteArrayOf(4, 5, 6), "image/png")
        assertNotEquals(a, b)
    }

    @Test
    fun inlineImageInequalityWithDifferentMimeType() {
        val data = byteArrayOf(1, 2, 3)
        val a = ImagenInlineImage(data, "image/png")
        val b = ImagenInlineImage(data, "image/jpeg")
        assertNotEquals(a, b)
    }

    @Test
    fun inlineImageNotEqualToNull() {
        val image = ImagenInlineImage(byteArrayOf(1), "image/png")
        assertFalse(image.equals(null))
    }

    @Test
    fun inlineImageNotEqualToOtherType() {
        val image = ImagenInlineImage(byteArrayOf(1), "image/png")
        assertFalse(image.equals("not an image"))
    }

    @Test
    fun inlineImageIdentityEquality() {
        val image = ImagenInlineImage(byteArrayOf(1), "image/png")
        assertTrue(image.equals(image))
    }

    @Test
    fun responseDefaultValues() {
        val response = ImagenGenerationResponse()
        assertTrue(response.images.isEmpty())
        assertNull(response.filteredReason)
    }

    @Test
    fun responseWithImages() {
        val images = listOf(
            ImagenInlineImage(byteArrayOf(1, 2), "image/png"),
            ImagenInlineImage(byteArrayOf(3, 4), "image/jpeg"),
        )
        val response = ImagenGenerationResponse(images = images)
        assertEquals(2, response.images.size)
        assertEquals("image/png", response.images[0].mimeType)
        assertEquals("image/jpeg", response.images[1].mimeType)
    }

    @Test
    fun responseWithFilteredReason() {
        val response = ImagenGenerationResponse(
            filteredReason = "Content was filtered due to safety concerns",
        )
        assertEquals("Content was filtered due to safety concerns", response.filteredReason)
    }

    @Test
    fun responseDataClassEquality() {
        val images = listOf(ImagenInlineImage(byteArrayOf(1, 2), "image/png"))
        val a = ImagenGenerationResponse(images = images, filteredReason = "reason")
        val b = ImagenGenerationResponse(images = images, filteredReason = "reason")
        assertEquals(a, b)
    }

    @Test
    fun imageFormatSealedClassEquality() {
        assertEquals(ImagenImageFormat.Png, ImagenImageFormat.png())
        assertEquals(ImagenImageFormat.Jpeg(80), ImagenImageFormat.jpeg(80))
        assertEquals(ImagenImageFormat.Jpeg(null), ImagenImageFormat.jpeg())
        assertNotEquals(ImagenImageFormat.Jpeg(80), ImagenImageFormat.Jpeg(90))
        assertNotEquals(ImagenImageFormat.Png as ImagenImageFormat, ImagenImageFormat.Jpeg() as ImagenImageFormat)
    }
}
