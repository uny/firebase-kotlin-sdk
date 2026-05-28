package dev.ynagai.firebase.ai

/**
 * Metadata about grounding sources used in generation.
 *
 * @property webSearchQueries The web search queries used for grounding.
 * @property searchEntryPoint The search entry point for web search results.
 * @property groundingChunks The grounding chunks referenced in the generated content.
 * @property groundingSupports Support information linking generated text to grounding chunks.
 */
data class GroundingMetadata(
    val webSearchQueries: List<String> = emptyList(),
    val searchEntryPoint: SearchEntryPoint? = null,
    val groundingChunks: List<GroundingChunk> = emptyList(),
    val groundingSupports: List<GroundingSupport> = emptyList(),
)

/**
 * An entry point for a web search result.
 *
 * @property renderedContent The rendered HTML content for the search entry point.
 */
data class SearchEntryPoint(val renderedContent: String)

/**
 * A chunk of grounding data.
 *
 * @property web The web grounding chunk, if this is a web-based source.
 * @property maps The Google Maps grounding chunk, if this is a maps-based source.
 */
data class GroundingChunk(
    val web: WebGroundingChunk? = null,
    val maps: GoogleMapsGroundingChunk? = null,
)

/**
 * A grounding chunk from Google Maps.
 *
 * @property uri The URI of the place.
 * @property title The title of the place.
 * @property placeId The place resource name in `places/{place_id}` format.
 */
data class GoogleMapsGroundingChunk(
    val uri: String? = null,
    val title: String? = null,
    val placeId: String? = null,
)

/**
 * A web-based grounding chunk with URL and title.
 *
 * @property uri The URI of the web source.
 * @property title The title of the web source.
 * @property domain The domain of the web source.
 */
data class WebGroundingChunk(
    val uri: String? = null,
    val title: String? = null,
    val domain: String? = null,
)

/**
 * Support information linking generated text to grounding sources.
 *
 * @property segment The text segment that is supported by grounding sources.
 * @property groundingChunkIndices The indices of grounding chunks that support this segment.
 */
data class GroundingSupport(
    val segment: Segment,
    val groundingChunkIndices: List<Int> = emptyList(),
)

/**
 * A segment of generated text with position information.
 *
 * @property partIndex The index of the part this segment belongs to.
 * @property startIndex The start index of the segment in the generated text.
 * @property endIndex The end index of the segment in the generated text.
 * @property text The text content of the segment.
 */
data class Segment(
    val partIndex: Int = 0,
    val startIndex: Int = 0,
    val endIndex: Int = 0,
    val text: String = "",
)
