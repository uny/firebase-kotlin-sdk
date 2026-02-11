package dev.ynagai.firebase.ai

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class GenerateContentResponseTest {

    @Test
    fun textReturnsFirstCandidateText() {
        val response = GenerateContentResponse(
            candidates = listOf(
                Candidate(
                    content = Content(
                        role = "model",
                        parts = listOf(TextPart("Hello, world!")),
                    ),
                ),
            ),
        )
        assertEquals("Hello, world!", response.text)
    }

    @Test
    fun textConcatenatesMultipleTextParts() {
        val response = GenerateContentResponse(
            candidates = listOf(
                Candidate(
                    content = Content(
                        role = "model",
                        parts = listOf(
                            TextPart("Hello, "),
                            TextPart("world!"),
                        ),
                    ),
                ),
            ),
        )
        assertEquals("Hello, world!", response.text)
    }

    @Test
    fun textReturnsNullForNoCandidates() {
        val response = GenerateContentResponse(candidates = emptyList())
        assertNull(response.text)
    }

    @Test
    fun textReturnsNullForNoTextParts() {
        val response = GenerateContentResponse(
            candidates = listOf(
                Candidate(
                    content = Content(
                        role = "model",
                        parts = listOf(InlineDataPart("image/png", byteArrayOf(1))),
                    ),
                ),
            ),
        )
        assertNull(response.text)
    }

    @Test
    fun textReturnsNullForEmptyParts() {
        val response = GenerateContentResponse(
            candidates = listOf(
                Candidate(
                    content = Content(role = "model", parts = emptyList()),
                ),
            ),
        )
        assertNull(response.text)
    }

    @Test
    fun textIgnoresNonTextParts() {
        val response = GenerateContentResponse(
            candidates = listOf(
                Candidate(
                    content = Content(
                        role = "model",
                        parts = listOf(
                            InlineDataPart("image/png", byteArrayOf(1)),
                            TextPart("Caption"),
                        ),
                    ),
                ),
            ),
        )
        assertEquals("Caption", response.text)
    }

    @Test
    fun usageMetadataDefaultsToZero() {
        val metadata = UsageMetadata()
        assertEquals(0, metadata.promptTokenCount)
        assertEquals(0, metadata.candidatesTokenCount)
        assertEquals(0, metadata.totalTokenCount)
    }

    @Test
    fun candidateDefaultValues() {
        val candidate = Candidate()
        assertNull(candidate.finishReason)
        assertEquals(emptyList(), candidate.safetyRatings)
        assertNull(candidate.citationMetadata)
    }

    @Test
    fun functionCallPartInResponse() {
        val response = GenerateContentResponse(
            candidates = listOf(
                Candidate(
                    content = Content(
                        role = "model",
                        parts = listOf(
                            FunctionCallPart("getWeather", mapOf("city" to "Tokyo")),
                        ),
                    ),
                ),
            ),
        )
        // text should be null when there are only function call parts
        assertNull(response.text)
        val part = response.candidates[0].content.parts[0]
        assertEquals("getWeather", (part as FunctionCallPart).name)
    }
}
