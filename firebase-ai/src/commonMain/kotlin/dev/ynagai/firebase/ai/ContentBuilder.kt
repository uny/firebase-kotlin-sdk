package dev.ynagai.firebase.ai

/**
 * DSL marker for the content builder DSL.
 */
@DslMarker
annotation class ContentDsl

/**
 * Builder class for constructing [Content] instances using a DSL.
 *
 * Use the [content] function to create content with this builder.
 *
 * @see content
 */
@ContentDsl
class ContentBuilder {
    private val parts = mutableListOf<Part>()

    /**
     * Adds a text part to the content.
     *
     * @param text The text to add.
     */
    fun text(text: String) {
        parts.add(TextPart(text))
    }

    /**
     * Adds an inline binary data part to the content.
     *
     * Use this for including images or other binary data.
     *
     * @param mimeType The MIME type of the data (e.g., "image/png").
     * @param data The raw binary data.
     */
    fun inlineData(mimeType: String, data: ByteArray) {
        parts.add(InlineDataPart(mimeType, data))
    }

    internal fun build(): List<Part> = parts.toList()
}

/**
 * Creates a [Content] instance using a DSL builder.
 *
 * @param role The role of the content creator. Defaults to "user".
 * @param block The builder block for adding parts.
 * @return A new [Content] instance.
 *
 * @sample
 * ```kotlin
 * val userContent = content {
 *     text("What's in this image?")
 *     inlineData("image/png", imageBytes)
 * }
 *
 * val systemContent = content(role = "system") {
 *     text("You are a helpful assistant.")
 * }
 * ```
 */
fun content(role: String? = "user", block: ContentBuilder.() -> Unit): Content {
    val builder = ContentBuilder()
    builder.block()
    return Content(role = role, parts = builder.build())
}
