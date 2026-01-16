package dev.ynagai.firebase.ai

data class GenerationConfig(
    val temperature: Float? = null,
    val topP: Float? = null,
    val topK: Int? = null,
    val candidateCount: Int? = null,
    val maxOutputTokens: Int? = null,
    val stopSequences: List<String>? = null,
    val responseMimeType: String? = null,
)

@DslMarker
annotation class GenerationConfigDsl

@GenerationConfigDsl
class GenerationConfigBuilder {
    var temperature: Float? = null
    var topP: Float? = null
    var topK: Int? = null
    var candidateCount: Int? = null
    var maxOutputTokens: Int? = null
    var stopSequences: List<String>? = null
    var responseMimeType: String? = null

    internal fun build(): GenerationConfig = GenerationConfig(
        temperature = temperature,
        topP = topP,
        topK = topK,
        candidateCount = candidateCount,
        maxOutputTokens = maxOutputTokens,
        stopSequences = stopSequences,
        responseMimeType = responseMimeType,
    )
}

fun generationConfig(block: GenerationConfigBuilder.() -> Unit): GenerationConfig {
    val builder = GenerationConfigBuilder()
    builder.block()
    return builder.build()
}
