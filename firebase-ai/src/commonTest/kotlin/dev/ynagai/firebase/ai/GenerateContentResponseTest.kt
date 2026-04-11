package dev.ynagai.firebase.ai

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
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
        assertEquals(0, metadata.thoughtsTokenCount)
        assertEquals(0, metadata.toolUsePromptTokenCount)
        assertEquals(0, metadata.cachedContentTokenCount)
        assertEquals(emptyList<ModalityTokenCount>(), metadata.promptTokensDetails)
        assertEquals(emptyList<ModalityTokenCount>(), metadata.candidatesTokensDetails)
        assertEquals(emptyList<ModalityTokenCount>(), metadata.toolUsePromptTokensDetails)
        assertEquals(emptyList<ModalityTokenCount>(), metadata.cacheTokensDetails)
    }

    @Test
    fun usageMetadataWithModalityDetails() {
        val details = listOf(
            ModalityTokenCount(ContentModality.TEXT, 100),
            ModalityTokenCount(ContentModality.IMAGE, 50),
        )
        val metadata = UsageMetadata(
            promptTokenCount = 150,
            totalTokenCount = 300,
            promptTokensDetails = details,
        )
        assertEquals(150, metadata.promptTokenCount)
        assertEquals(2, metadata.promptTokensDetails.size)
        assertEquals(ContentModality.TEXT, metadata.promptTokensDetails[0].modality)
        assertEquals(100, metadata.promptTokensDetails[0].tokenCount)
    }

    @Test
    fun candidateDefaultValues() {
        val candidate = Candidate()
        assertNull(candidate.finishReason)
        assertEquals(emptyList(), candidate.safetyRatings)
        assertNull(candidate.citationMetadata)
        assertNull(candidate.groundingMetadata)
        assertNull(candidate.urlContextMetadata)
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

    @Test
    fun functionCallsReturnsFunctionCallParts() {
        val response = GenerateContentResponse(
            candidates = listOf(
                Candidate(
                    content = Content(
                        role = "model",
                        parts = listOf(
                            FunctionCallPart("getWeather", mapOf("city" to "Tokyo")),
                            FunctionCallPart("getTime", mapOf("zone" to "JST")),
                        ),
                    ),
                ),
            ),
        )
        val calls = response.functionCalls
        assertNotNull(calls)
        assertEquals(2, calls.size)
        assertEquals("getWeather", calls[0].name)
        assertEquals("getTime", calls[1].name)
    }

    @Test
    fun functionCallsReturnsNullForNoCandidates() {
        val response = GenerateContentResponse(candidates = emptyList())
        assertNull(response.functionCalls)
    }

    @Test
    fun functionCallsReturnsNullForNoFunctionCallParts() {
        val response = GenerateContentResponse(
            candidates = listOf(
                Candidate(
                    content = Content(
                        role = "model",
                        parts = listOf(TextPart("Hello")),
                    ),
                ),
            ),
        )
        assertNull(response.functionCalls)
    }

    @Test
    fun functionCallsIgnoresNonFunctionCallParts() {
        val response = GenerateContentResponse(
            candidates = listOf(
                Candidate(
                    content = Content(
                        role = "model",
                        parts = listOf(
                            TextPart("Let me call a function"),
                            FunctionCallPart("search", mapOf("q" to "kotlin")),
                        ),
                    ),
                ),
            ),
        )
        val calls = response.functionCalls
        assertNotNull(calls)
        assertEquals(1, calls.size)
        assertEquals("search", calls[0].name)
    }
}
