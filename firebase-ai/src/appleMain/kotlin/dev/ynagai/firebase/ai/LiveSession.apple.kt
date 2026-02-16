package dev.ynagai.firebase.ai

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.suspendCancellableCoroutine
import swiftPMImport.dev.ynagai.firebase.firebase.ai.KFBLiveServerMessage
import swiftPMImport.dev.ynagai.firebase.firebase.ai.KFBLiveSession
import kotlin.coroutines.resume

@OptIn(ExperimentalForeignApi::class)
actual class LiveSession internal constructor(
    internal val apple: KFBLiveSession,
) {
    actual fun receive(): Flow<LiveServerMessage> = flow {
        val stream = apple.responses()
        while (true) {
            val message = awaitNullableResult<KFBLiveServerMessage> { callback ->
                stream.nextWithCompletionHandler(callback)
            } ?: break
            emit(message.toCommon())
        }
    }

    actual suspend fun send(content: Content, turnComplete: Boolean) {
        suspendCancellableCoroutine { continuation ->
            apple.sendContent(
                listOf(content.toApple()),
                turnComplete = turnComplete,
            ) {
                continuation.resume(Unit)
            }
        }
    }

    actual suspend fun sendTextRealtime(text: String) {
        suspendCancellableCoroutine { continuation ->
            apple.sendTextRealtime(text) {
                continuation.resume(Unit)
            }
        }
    }

    actual suspend fun sendMediaRealtime(data: InlineDataPart) {
        suspendCancellableCoroutine { continuation ->
            if (data.mimeType.startsWith("audio/")) {
                apple.sendAudioRealtime(data.data.toNSData()) {
                    continuation.resume(Unit)
                }
            } else {
                apple.sendVideoRealtime(data.data.toNSData(), mimeType = data.mimeType) {
                    continuation.resume(Unit)
                }
            }
        }
    }

    actual suspend fun sendFunctionResponses(functionList: List<FunctionResponsePart>) {
        suspendCancellableCoroutine { continuation ->
            apple.sendFunctionResponses(functionList.map { it.toAppleFunctionResponse() }) {
                continuation.resume(Unit)
            }
        }
    }

    actual fun close() {
        apple.closeWithCompletionHandler { }
    }
}
