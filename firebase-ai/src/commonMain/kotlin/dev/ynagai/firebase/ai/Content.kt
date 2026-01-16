package dev.ynagai.firebase.ai

data class Content(
    val role: String? = null,
    val parts: List<Part> = emptyList(),
)

sealed interface Part

data class TextPart(val text: String) : Part

data class InlineDataPart(
    val mimeType: String,
    val data: ByteArray,
) : Part {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false
        other as InlineDataPart
        if (mimeType != other.mimeType) return false
        if (!data.contentEquals(other.data)) return false
        return true
    }

    override fun hashCode(): Int {
        var result = mimeType.hashCode()
        result = 31 * result + data.contentHashCode()
        return result
    }
}
