package dev.ynagai.firebase.ai

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ImagenGenerationConfigTest {

    @Test
    fun defaultValuesAreNull() {
        val config = imagenGenerationConfig { }
        assertNull(config.negativePrompt)
        assertNull(config.numberOfImages)
        assertNull(config.aspectRatio)
        assertNull(config.imageFormat)
        assertNull(config.addWatermark)
    }

    @Test
    fun negativePromptIsSet() {
        val config = imagenGenerationConfig {
            negativePrompt = "blurry, low quality"
        }
        assertEquals("blurry, low quality", config.negativePrompt)
    }

    @Test
    fun numberOfImagesIsSet() {
        val config = imagenGenerationConfig {
            numberOfImages = 4
        }
        assertEquals(4, config.numberOfImages)
    }

    @Test
    fun aspectRatioIsSet() {
        val config = imagenGenerationConfig {
            aspectRatio = ImagenAspectRatio.LANDSCAPE_16x9
        }
        assertEquals(ImagenAspectRatio.LANDSCAPE_16x9, config.aspectRatio)
    }

    @Test
    fun imageFormatPngIsSet() {
        val config = imagenGenerationConfig {
            imageFormat = ImagenImageFormat.png()
        }
        assertEquals(ImagenImageFormat.Png, config.imageFormat)
    }

    @Test
    fun imageFormatJpegIsSet() {
        val config = imagenGenerationConfig {
            imageFormat = ImagenImageFormat.jpeg(compressionQuality = 80)
        }
        assertEquals(ImagenImageFormat.Jpeg(80), config.imageFormat)
    }

    @Test
    fun imageFormatJpegDefaultQuality() {
        val config = imagenGenerationConfig {
            imageFormat = ImagenImageFormat.jpeg()
        }
        assertEquals(ImagenImageFormat.Jpeg(null), config.imageFormat)
    }

    @Test
    fun addWatermarkIsSet() {
        val config = imagenGenerationConfig {
            addWatermark = true
        }
        assertEquals(true, config.addWatermark)
    }

    @Test
    fun addWatermarkFalseIsDistinctFromNull() {
        val configFalse = imagenGenerationConfig {
            addWatermark = false
        }
        val configNull = imagenGenerationConfig { }
        assertEquals(false, configFalse.addWatermark)
        assertNull(configNull.addWatermark)
    }

    @Test
    fun allPropertiesCanBeSet() {
        val config = imagenGenerationConfig {
            negativePrompt = "bad quality"
            numberOfImages = 2
            aspectRatio = ImagenAspectRatio.SQUARE_1x1
            imageFormat = ImagenImageFormat.png()
            addWatermark = false
        }
        assertEquals("bad quality", config.negativePrompt)
        assertEquals(2, config.numberOfImages)
        assertEquals(ImagenAspectRatio.SQUARE_1x1, config.aspectRatio)
        assertEquals(ImagenImageFormat.Png, config.imageFormat)
        assertEquals(false, config.addWatermark)
    }

    @Test
    fun dataClassCopyWorks() {
        val original = imagenGenerationConfig {
            numberOfImages = 2
            aspectRatio = ImagenAspectRatio.SQUARE_1x1
        }
        val copy = original.copy(numberOfImages = 4)
        assertEquals(4, copy.numberOfImages)
        assertEquals(ImagenAspectRatio.SQUARE_1x1, copy.aspectRatio)
    }

    @Test
    fun allAspectRatioValues() {
        val values = ImagenAspectRatio.entries
        assertEquals(5, values.size)
        assertEquals(
            listOf(
                ImagenAspectRatio.SQUARE_1x1,
                ImagenAspectRatio.PORTRAIT_9x16,
                ImagenAspectRatio.LANDSCAPE_16x9,
                ImagenAspectRatio.PORTRAIT_3x4,
                ImagenAspectRatio.LANDSCAPE_4x3,
            ),
            values,
        )
    }

    @Test
    fun allSafetyFilterLevelValues() {
        val values = ImagenSafetyFilterLevel.entries
        assertEquals(4, values.size)
    }

    @Test
    fun allPersonFilterLevelValues() {
        val values = ImagenPersonFilterLevel.entries
        assertEquals(3, values.size)
    }

    @Test
    fun safetySettingsWithValues() {
        val settings = ImagenSafetySettings(
            safetyFilterLevel = ImagenSafetyFilterLevel.BLOCK_MEDIUM_AND_ABOVE,
            personFilterLevel = ImagenPersonFilterLevel.ALLOW_ADULT,
        )
        assertEquals(ImagenSafetyFilterLevel.BLOCK_MEDIUM_AND_ABOVE, settings.safetyFilterLevel)
        assertEquals(ImagenPersonFilterLevel.ALLOW_ADULT, settings.personFilterLevel)
    }
}
