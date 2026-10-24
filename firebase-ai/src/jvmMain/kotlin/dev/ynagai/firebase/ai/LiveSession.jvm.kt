package dev.ynagai.firebase.ai

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

// Not reachable: LiveGenerativeModel.connect() always fails on JVM.
@Suppress("UNUSED_PARAMETER")
actual class LiveSession private constructor() {
    actual fun receive(): Flow<LiveServerMessage> = flow { unavailableOnJvm() }

    actual suspend fun send(content: Content, turnComplete: Boolean): Unit = unavailableOnJvm()

    actual suspend fun sendTextRealtime(text: String): Unit = unavailableOnJvm()

    actual suspend fun sendMediaRealtime(data: InlineDataPart): Unit = unavailableOnJvm()

    actual suspend fun sendFunctionResponses(functionList: List<FunctionResponsePart>): Unit =
        unavailableOnJvm()

    actual suspend fun resumeSession(sessionResumption: SessionResumptionConfig?): Unit =
        unavailableOnJvm()

    actual fun close() = Unit
}
