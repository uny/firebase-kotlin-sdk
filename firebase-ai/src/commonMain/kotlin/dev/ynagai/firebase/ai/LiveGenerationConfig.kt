package dev.ynagai.firebase.ai

/**
 * Configuration options for live (real-time streaming) content generation.
 *
 * Use [liveGenerationConfig] DSL function to create instances.
 *
 * @property responseModality The modality for generated content (e.g., TEXT, AUDIO).
 * @property speechConfig Optional speech configuration for audio output.
 * @property temperature Controls randomness in output. Higher values produce more
 *                       creative outputs, while lower values produce more deterministic outputs.
 * @property topK Limits token selection to the top K most probable tokens.
 * @property topP Nucleus sampling threshold.
 * @property maxOutputTokens Maximum number of tokens to generate in the response.
 */
data class LiveGenerationConfig(
    val responseModality: ResponseModality? = null,
    val speechConfig: SpeechConfig? = null,
    val temperature: Float? = null,
    val topK: Int? = null,
    val topP: Float? = null,
    val maxOutputTokens: Int? = null,
)

/**
 * Configuration for speech output in live generation.
 *
 * @property voice The voice to use for speech output.
 */
data class SpeechConfig(val voice: Voice)

/**
 * A voice configuration for speech output.
 *
 * @property name The name of the voice (e.g., "Puck", "Charon", "Kore", "Fenrir", "Aoede").
 */
data class Voice(val name: String)

/**
 * Builder class for constructing [LiveGenerationConfig] instances using a DSL.
 *
 * @see liveGenerationConfig
 */
@GenerationConfigDsl
class LiveGenerationConfigBuilder {
    /** The modality for generated content (e.g., TEXT, AUDIO). */
    var responseModality: ResponseModality? = null
    /** Speech configuration for audio output. */
    var speechConfig: SpeechConfig? = null
    /** Controls randomness in output. */
    var temperature: Float? = null
    /** Limits token selection to top K tokens. */
    var topK: Int? = null
    /** Nucleus sampling threshold. */
    var topP: Float? = null
    /** Maximum number of tokens in the response. */
    var maxOutputTokens: Int? = null

    internal fun build(): LiveGenerationConfig = LiveGenerationConfig(
        responseModality = responseModality,
        speechConfig = speechConfig,
        temperature = temperature,
        topK = topK,
        topP = topP,
        maxOutputTokens = maxOutputTokens,
    )
}

/**
 * Creates a [LiveGenerationConfig] using a DSL builder.
 *
 * @param block The builder block for configuring live generation parameters.
 * @return A new [LiveGenerationConfig] instance.
 *
 * Example usage:
 * ```kotlin
 * val config = liveGenerationConfig {
 *     responseModality = ResponseModality.AUDIO
 *     speechConfig = SpeechConfig(Voice("Puck"))
 *     temperature = 0.7f
 * }
 * ```
 */
fun liveGenerationConfig(block: LiveGenerationConfigBuilder.() -> Unit): LiveGenerationConfig {
    val builder = LiveGenerationConfigBuilder()
    builder.block()
    return builder.build()
}
