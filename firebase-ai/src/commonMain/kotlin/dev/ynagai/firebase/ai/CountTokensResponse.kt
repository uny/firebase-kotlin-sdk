package dev.ynagai.firebase.ai

/**
 * Response from a token counting request.
 *
 * Use [GenerativeModel.countTokens] to count tokens in content
 * before sending it to the model.
 *
 * @property totalTokens The total number of tokens in the content.
 *
 * @sample
 * ```kotlin
 * val response = model.countTokens("Hello, world!")
 * println("Tokens: ${response.totalTokens}")
 * ```
 */
data class CountTokensResponse(
    val totalTokens: Int,
)
