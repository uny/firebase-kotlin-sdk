package dev.ynagai.firebase.ai

/**
 * Metadata for URL context used in generation.
 *
 * @property urlMetadata The list of URL metadata entries.
 */
data class UrlContextMetadata(
    val urlMetadata: List<UrlMetadata> = emptyList(),
)

/**
 * Metadata about a retrieved URL.
 *
 * @property retrievedUrl The URL that was retrieved.
 * @property retrievalStatus The status of the URL retrieval operation.
 */
data class UrlMetadata(
    val retrievedUrl: String? = null,
    val retrievalStatus: UrlRetrievalStatus = UrlRetrievalStatus.UNSPECIFIED,
)

/**
 * Status of a URL retrieval operation.
 */
enum class UrlRetrievalStatus {
    SUCCESS,
    ERROR,
    PAYWALL,
    UNSAFE,
    UNSPECIFIED,
}
