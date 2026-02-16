package dev.ynagai.firebase.ai

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import swiftPMImport.dev.ynagai.firebase.firebase.ai.KFBLiveServerMessage
import swiftPMImport.dev.ynagai.firebase.firebase.ai.KFBLiveSession

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
        awaitVoid { callback ->
            apple.sendContent(
                listOf(content.toApple()),
                turnComplete = turnComplete,
                completionHandler = callback,
            )
        }
    }

    actual suspend fun sendTextRealtime(text: String) {
        awaitVoid { callback ->
            apple.sendTextRealtime(text, completionHandler = callback)
        }
    }

    actual suspend fun sendMediaRealtime(data: InlineDataPart) {
        when {
            data.mimeType.startsWith("audio/") -> {
                awaitVoid { callback ->
                    apple.sendAudioRealtime(data.data.toNSData(), completionHandler = callback)
                }
            }
            data.mimeType.startsWith("video/") -> {
                awaitVoid { callback ->
                    apple.sendVideoRealtime(
                        data.data.toNSData(),
                        mimeType = data.mimeType,
                        completionHandler = callback,
                    )
                }
            }
            else -> throw IllegalArgumentException(
                "Unsupported mimeType for sendMediaRealtime: ${data.mimeType}. " +
                    "Only audio/* and video/* are supported.",
            )
        }
    }

    actual suspend fun sendFunctionResponses(functionList: List<FunctionResponsePart>) {
        awaitVoid { callback ->
            apple.sendFunctionResponses(
                functionList.map { it.toAppleFunctionResponse() },
                completionHandler = callback,
            )
        }
    }

    actual fun close() {
        apple.closeWithCompletionHandler { }
    }
}
