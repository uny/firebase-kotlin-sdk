package dev.ynagai.firebase.ai

/**
 * Configuration options for content generation.
 *
 * Use [generationConfig] DSL function to create instances.
 *
 * @property temperature Controls randomness in output. Higher values (e.g., 0.9) produce more
 *                       creative outputs, while lower values (e.g., 0.1) produce more deterministic outputs.
 *                       Valid range: 0.0 to 2.0.
 * @property topP Nucleus sampling threshold. The model considers tokens with cumulative probability
 *                up to this value. Valid range: 0.0 to 1.0.
 * @property topK Limits token selection to the top K most probable tokens.
 * @property candidateCount Number of response candidates to generate.
 * @property maxOutputTokens Maximum number of tokens to generate in the response.
 * @property stopSequences Sequences that will stop generation when encountered.
 * @property responseMimeType The desired MIME type of the response (e.g., "application/json").
 */
data class GenerationConfig(
    val temperature: Float? = null,
    val topP: Float? = null,
    val topK: Int? = null,
    val candidateCount: Int? = null,
    val maxOutputTokens: Int? = null,
    val stopSequences: List<String>? = null,
    val responseMimeType: String? = null,
    val responseSchema: Schema? = null,
)

/**
 * DSL marker for the generation config builder.
 */
@DslMarker
annotation class GenerationConfigDsl

/**
 * Builder class for constructing [GenerationConfig] instances using a DSL.
 *
 * @see generationConfig
 */
@GenerationConfigDsl
class GenerationConfigBuilder {
    /** Controls randomness in output. Range: 0.0 to 2.0. */
    var temperature: Float? = null
    /** Nucleus sampling threshold. Range: 0.0 to 1.0. */
    var topP: Float? = null
    /** Limits token selection to top K tokens. */
    var topK: Int? = null
    /** Number of response candidates to generate. */
    var candidateCount: Int? = null
    /** Maximum number of tokens in the response. */
    var maxOutputTokens: Int? = null
    /** Sequences that stop generation when encountered. */
    var stopSequences: List<String>? = null
    /** Desired MIME type of the response. */
    var responseMimeType: String? = null
    /** Schema for structured JSON response output. */
    var responseSchema: Schema? = null

    internal fun build(): GenerationConfig = GenerationConfig(
        temperature = temperature,
        topP = topP,
        topK = topK,
        candidateCount = candidateCount,
        maxOutputTokens = maxOutputTokens,
        stopSequences = stopSequences,
        responseMimeType = responseMimeType,
        responseSchema = responseSchema,
    )
}

/**
 * Creates a [GenerationConfig] using a DSL builder.
 *
 * @param block The builder block for configuring generation parameters.
 * @return A new [GenerationConfig] instance.
 *
 * @sample
 * ```kotlin
 * val config = generationConfig {
 *     temperature = 0.7f
 *     maxOutputTokens = 1024
 *     topP = 0.9f
 *     stopSequences = listOf("END", "STOP")
 * }
 *
 * val model = ai.generativeModel(
 *     modelName = "gemini-2.0-flash",
 *     generationConfig = config
 * )
 * ```
 */
fun generationConfig(block: GenerationConfigBuilder.() -> Unit): GenerationConfig {
    val builder = GenerationConfigBuilder()
    builder.block()
    return builder.build()
}
