package dev.ynagai.firebase.ai

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class GenerationConfigTest {

    @Test
    fun defaultValuesAreNull() {
        val config = generationConfig { }
        assertNull(config.temperature)
        assertNull(config.topP)
        assertNull(config.topK)
        assertNull(config.candidateCount)
        assertNull(config.maxOutputTokens)
        assertNull(config.stopSequences)
        assertNull(config.responseMimeType)
        assertNull(config.responseSchema)
    }

    @Test
    fun temperatureIsSet() {
        val config = generationConfig {
            temperature = 0.7f
        }
        assertEquals(0.7f, config.temperature)
    }

    @Test
    fun topPIsSet() {
        val config = generationConfig {
            topP = 0.9f
        }
        assertEquals(0.9f, config.topP)
    }

    @Test
    fun topKIsSet() {
        val config = generationConfig {
            topK = 40
        }
        assertEquals(40, config.topK)
    }

    @Test
    fun maxOutputTokensIsSet() {
        val config = generationConfig {
            maxOutputTokens = 1024
        }
        assertEquals(1024, config.maxOutputTokens)
    }

    @Test
    fun stopSequencesAreSet() {
        val config = generationConfig {
            stopSequences = listOf("END", "STOP")
        }
        assertEquals(listOf("END", "STOP"), config.stopSequences)
    }

    @Test
    fun responseMimeTypeIsSet() {
        val config = generationConfig {
            responseMimeType = "application/json"
        }
        assertEquals("application/json", config.responseMimeType)
    }

    @Test
    fun responseSchemaIsSet() {
        val schema = Schema.obj(
            properties = mapOf("name" to Schema.string()),
            requiredProperties = listOf("name"),
        )
        val config = generationConfig {
            responseMimeType = "application/json"
            responseSchema = schema
        }
        assertEquals(schema, config.responseSchema)
    }

    @Test
    fun allPropertiesCanBeSet() {
        val config = generationConfig {
            temperature = 0.5f
            topP = 0.8f
            topK = 20
            candidateCount = 2
            maxOutputTokens = 512
            stopSequences = listOf("DONE")
            responseMimeType = "text/plain"
        }
        assertEquals(0.5f, config.temperature)
        assertEquals(0.8f, config.topP)
        assertEquals(20, config.topK)
        assertEquals(2, config.candidateCount)
        assertEquals(512, config.maxOutputTokens)
        assertEquals(listOf("DONE"), config.stopSequences)
        assertEquals("text/plain", config.responseMimeType)
    }

    @Test
    fun dataClassCopyWorks() {
        val original = generationConfig {
            temperature = 0.7f
            maxOutputTokens = 1024
        }
        val copy = original.copy(temperature = 0.9f)
        assertEquals(0.9f, copy.temperature)
        assertEquals(1024, copy.maxOutputTokens)
    }
}
