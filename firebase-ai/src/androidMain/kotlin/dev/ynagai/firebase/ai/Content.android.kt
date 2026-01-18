package dev.ynagai.firebase.ai

import com.google.firebase.ai.type.content as androidContent
import com.google.firebase.ai.type.Content as AndroidContent
import com.google.firebase.ai.type.Part as AndroidPart
import com.google.firebase.ai.type.TextPart as AndroidTextPart
import com.google.firebase.ai.type.InlineDataPart as AndroidInlineDataPart

internal fun Content.toAndroid(): AndroidContent {
    val contentParts = this.parts
    return androidContent(role ?: "user") {
        for (part in contentParts) {
            when (part) {
                is TextPart -> text(part.text)
                is InlineDataPart -> inlineData(part.data, part.mimeType)
            }
        }
    }
}

internal fun AndroidContent.toCommon(): Content = Content(
    role = role,
    parts = parts.map { it.toCommon() },
)

internal fun AndroidPart.toCommon(): Part = when (this) {
    is AndroidTextPart -> TextPart(text)
    is AndroidInlineDataPart -> InlineDataPart(mimeType, inlineData)
    else -> throw IllegalArgumentException("Unknown AndroidPart type: ${this::class.simpleName}")
}
