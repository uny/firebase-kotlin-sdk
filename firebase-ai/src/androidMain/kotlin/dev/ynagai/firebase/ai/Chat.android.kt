package dev.ynagai.firebase.ai

import com.google.firebase.ai.Chat as AndroidChat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

actual class Chat internal constructor(
    internal val android: AndroidChat
) {
    actual val history: List<Content>
        get() = android.history.map { it.toCommon() }

    actual suspend fun sendMessage(content: Content): GenerateContentResponse =
        android.sendMessage(content.toAndroid()).toCommon()

    actual suspend fun sendMessage(prompt: String): GenerateContentResponse =
        android.sendMessage(prompt).toCommon()

    actual fun sendMessageStream(content: Content): Flow<GenerateContentResponse> =
        android.sendMessageStream(content.toAndroid()).map { it.toCommon() }

    actual fun sendMessageStream(prompt: String): Flow<GenerateContentResponse> =
        android.sendMessageStream(prompt).map { it.toCommon() }
}
