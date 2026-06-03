package dev.ynagai.firebase.ai

import com.google.firebase.ai.type.CodeExecutionResultPart as AndroidCodeExecutionResultPart
import com.google.firebase.ai.type.content as androidContent
import com.google.firebase.ai.type.Content as AndroidContent
import com.google.firebase.ai.type.ExecutableCodePart as AndroidExecutableCodePart
import com.google.firebase.ai.type.FileDataPart as AndroidFileDataPart
import com.google.firebase.ai.type.FunctionCallPart as AndroidFunctionCallPart
import com.google.firebase.ai.type.FunctionResponsePart as AndroidFunctionResponsePart
import com.google.firebase.ai.type.Part as AndroidPart
import com.google.firebase.ai.type.TextPart as AndroidTextPart
import com.google.firebase.ai.type.InlineDataPart as AndroidInlineDataPart
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.longOrNull

internal fun Content.toAndroid(): AndroidContent {
    val contentParts = this.parts
    return androidContent(role ?: "user") {
        for (part in contentParts) {
            when (part) {
                // isThought is response-only: the 3-arg AndroidTextPart constructor that
                // carries it is internal in firebase-ai, so we cannot send thought back.
                is TextPart -> text(part.text)
                is InlineDataPart -> inlineData(part.data, part.mimeType)
                is FileDataPart -> part(AndroidFileDataPart(part.mimeType, part.uri))
                is FunctionCallPart -> part(
                    AndroidFunctionCallPart(
                        part.name,
                        part.args.mapValues { (_, v) -> v.toJsonElement() },
                        part.id,
                    )
                )
                is FunctionResponsePart -> part(
                    AndroidFunctionResponsePart(
                        part.name,
                        JsonObject(part.response.mapValues { (_, v) -> v.toJsonElement() }),
                        part.id,
                    )
                )
                is ExecutableCodePart -> {} // Response-only part; not sent to model
                is CodeExecutionResultPart -> {} // Response-only part; not sent to model
            }
        }
    }
}

internal fun AndroidContent.toCommon(): Content = Content(
    role = role,
    parts = parts.map { it.toCommon() },
)

internal fun AndroidPart.toCommon(): Part = when (this) {
    is AndroidTextPart -> TextPart(text = text, isThought = isThought)
    is AndroidInlineDataPart -> InlineDataPart(mimeType, inlineData, displayName, isThought)
    is AndroidFileDataPart -> FileDataPart(mimeType, uri, isThought)
    is AndroidFunctionCallPart -> toCommon()
    is AndroidFunctionResponsePart -> FunctionResponsePart(
        name = name,
        response = response.mapValues { (_, v) -> v.toAny() },
        id = id,
        isThought = isThought,
    )
    is AndroidExecutableCodePart -> ExecutableCodePart(
        language = language,
        code = code,
        isThought = isThought,
    )
    is AndroidCodeExecutionResultPart -> CodeExecutionResultPart(
        outcome = when {
            outcome.equals("outcome_ok", ignoreCase = true) -> CodeExecutionOutcome.OK
            outcome.equals("outcome_failed", ignoreCase = true) -> CodeExecutionOutcome.FAILED
            outcome.equals("outcome_deadline_exceeded", ignoreCase = true) -> CodeExecutionOutcome.DEADLINE_EXCEEDED
            else -> CodeExecutionOutcome.UNSPECIFIED
        },
        output = output,
        isThought = isThought,
    )
    else -> throw IllegalArgumentException("Unknown AndroidPart type: ${this::class.simpleName}")
}

internal fun AndroidFunctionCallPart.toCommon(): FunctionCallPart = FunctionCallPart(
    name = name,
    args = args.mapValues { (_, v) -> v.toAny() },
    id = id,
    isThought = isThought,
)

private fun Any?.toJsonElement(): JsonElement = when (this) {
    null -> JsonNull
    is String -> JsonPrimitive(this)
    is Number -> JsonPrimitive(this)
    is Boolean -> JsonPrimitive(this)
    is Map<*, *> -> JsonObject(entries.associate { (k, v) -> k.toString() to v.toJsonElement() })
    is List<*> -> JsonArray(map { it.toJsonElement() })
    else -> JsonPrimitive(toString())
}

private fun JsonElement.toAny(): Any? = when (this) {
    is JsonNull -> null
    is JsonPrimitive -> if (isString) content else booleanOrNull ?: longOrNull ?: doubleOrNull ?: content
    is JsonObject -> mapValues { (_, v) -> v.toAny() }
    is JsonArray -> map { it.toAny() }
}
