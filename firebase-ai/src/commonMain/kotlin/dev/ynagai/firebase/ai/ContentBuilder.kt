package dev.ynagai.firebase.ai

@DslMarker
annotation class ContentDsl

@ContentDsl
class ContentBuilder {
    private val parts = mutableListOf<Part>()

    fun text(text: String) {
        parts.add(TextPart(text))
    }

    fun inlineData(mimeType: String, data: ByteArray) {
        parts.add(InlineDataPart(mimeType, data))
    }

    internal fun build(): List<Part> = parts.toList()
}

fun content(role: String? = "user", block: ContentBuilder.() -> Unit): Content {
    val builder = ContentBuilder()
    builder.block()
    return Content(role = role, parts = builder.build())
}
