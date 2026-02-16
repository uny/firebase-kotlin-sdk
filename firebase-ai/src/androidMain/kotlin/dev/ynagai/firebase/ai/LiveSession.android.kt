@file:OptIn(PublicPreviewAPI::class)

package dev.ynagai.firebase.ai

import com.google.firebase.ai.type.FunctionResponsePart as AndroidFunctionResponsePart
import com.google.firebase.ai.type.LiveSession as AndroidLiveSession
import com.google.firebase.ai.type.PublicPreviewAPI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonObject

actual class LiveSession internal constructor(
    internal val android: AndroidLiveSession,
) {
    actual fun receive(): Flow<LiveServerMessage> =
        android.receive()
            .map { it.toCommon() }
            .catch { throw mapAndroidException(it) }

    actual suspend fun send(content: Content, turnComplete: Boolean): Unit =
        wrapAndroidException {
            android.send(content.toAndroid())
        }

    actual suspend fun sendTextRealtime(text: String): Unit =
        wrapAndroidException {
            android.sendTextRealtime(text)
        }

    actual suspend fun sendMediaRealtime(data: InlineDataPart): Unit =
        wrapAndroidException {
            val inlineData = data.toAndroidInlineData()
            when {
                data.mimeType.startsWith("audio/") -> android.sendAudioRealtime(inlineData)
                data.mimeType.startsWith("video/") -> android.sendVideoRealtime(inlineData)
                else -> throw IllegalArgumentException(
                    "Unsupported mimeType for sendMediaRealtime: ${data.mimeType}. " +
                        "Only audio/* and video/* are supported.",
                )
            }
        }

    actual suspend fun sendFunctionResponses(functionList: List<FunctionResponsePart>): Unit =
        wrapAndroidException {
            android.sendFunctionResponse(functionList.map { it.toAndroidFunctionResponsePart() })
        }

    actual fun close() {
        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            runCatching { android.close() }
        }
    }
}

private fun Any?.toJsonElement(): kotlinx.serialization.json.JsonElement = when (this) {
    null -> kotlinx.serialization.json.JsonNull
    is String -> kotlinx.serialization.json.JsonPrimitive(this)
    is Number -> kotlinx.serialization.json.JsonPrimitive(this)
    is Boolean -> kotlinx.serialization.json.JsonPrimitive(this)
    is Map<*, *> -> JsonObject(entries.associate { (k, v) -> k.toString() to v.toJsonElement() })
    is List<*> -> kotlinx.serialization.json.JsonArray(map { it.toJsonElement() })
    else -> kotlinx.serialization.json.JsonPrimitive(toString())
}

private fun FunctionResponsePart.toAndroidFunctionResponsePart(): AndroidFunctionResponsePart =
    AndroidFunctionResponsePart(
        name = name,
        response = JsonObject(response.mapValues { (_, v) -> v.toJsonElement() }),
    )
