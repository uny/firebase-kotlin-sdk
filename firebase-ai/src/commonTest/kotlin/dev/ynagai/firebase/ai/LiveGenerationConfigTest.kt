package dev.ynagai.firebase.ai

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class LiveGenerationConfigTest {

    @Test
    fun defaultValuesAreNull() {
        val config = liveGenerationConfig { }
        assertNull(config.responseModality)
        assertNull(config.speechConfig)
        assertNull(config.temperature)
        assertNull(config.topK)
        assertNull(config.topP)
        assertNull(config.maxOutputTokens)
    }

    @Test
    fun responseModalityIsSet() {
        val config = liveGenerationConfig {
            responseModality = ResponseModality.TEXT
        }
        assertEquals(ResponseModality.TEXT, config.responseModality)
    }

    @Test
    fun audioModalityIsSet() {
        val config = liveGenerationConfig {
            responseModality = ResponseModality.AUDIO
        }
        assertEquals(ResponseModality.AUDIO, config.responseModality)
    }

    @Test
    fun speechConfigIsSet() {
        val config = liveGenerationConfig {
            speechConfig = SpeechConfig(Voice("Puck"))
        }
        assertEquals(SpeechConfig(Voice("Puck")), config.speechConfig)
        assertEquals("Puck", config.speechConfig?.voice?.name)
    }

    @Test
    fun temperatureIsSet() {
        val config = liveGenerationConfig {
            temperature = 0.7f
        }
        assertEquals(0.7f, config.temperature)
    }

    @Test
    fun topKIsSet() {
        val config = liveGenerationConfig {
            topK = 40
        }
        assertEquals(40, config.topK)
    }

    @Test
    fun topPIsSet() {
        val config = liveGenerationConfig {
            topP = 0.9f
        }
        assertEquals(0.9f, config.topP)
    }

    @Test
    fun maxOutputTokensIsSet() {
        val config = liveGenerationConfig {
            maxOutputTokens = 1024
        }
        assertEquals(1024, config.maxOutputTokens)
    }

    @Test
    fun allPropertiesCanBeSet() {
        val config = liveGenerationConfig {
            responseModality = ResponseModality.AUDIO
            speechConfig = SpeechConfig(Voice("Charon"))
            temperature = 0.5f
            topK = 20
            topP = 0.8f
            maxOutputTokens = 512
        }
        assertEquals(ResponseModality.AUDIO, config.responseModality)
        assertEquals(SpeechConfig(Voice("Charon")), config.speechConfig)
        assertEquals(0.5f, config.temperature)
        assertEquals(20, config.topK)
        assertEquals(0.8f, config.topP)
        assertEquals(512, config.maxOutputTokens)
    }

    @Test
    fun dataClassCopyWorks() {
        val original = liveGenerationConfig {
            responseModality = ResponseModality.TEXT
            temperature = 0.7f
        }
        val copy = original.copy(temperature = 0.9f)
        assertEquals(0.9f, copy.temperature)
        assertEquals(ResponseModality.TEXT, copy.responseModality)
    }

    @Test
    fun voiceEquality() {
        assertEquals(Voice("Puck"), Voice("Puck"))
    }

    @Test
    fun speechConfigEquality() {
        assertEquals(
            SpeechConfig(Voice("Kore")),
            SpeechConfig(Voice("Kore")),
        )
    }
}
