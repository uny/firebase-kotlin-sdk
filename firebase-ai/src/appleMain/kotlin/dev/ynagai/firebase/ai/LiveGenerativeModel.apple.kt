package dev.ynagai.firebase.ai

import kotlinx.cinterop.ExperimentalForeignApi
import swiftPMImport.dev.ynagai.firebase.firebase.ai.KFBLiveGenerativeModel
import swiftPMImport.dev.ynagai.firebase.firebase.ai.KFBLiveSession

@OptIn(ExperimentalForeignApi::class)
actual class LiveGenerativeModel internal constructor(
    internal val apple: KFBLiveGenerativeModel,
) {
    actual suspend fun connect(): LiveSession {
        val result = awaitResult<KFBLiveSession> { callback ->
            apple.connectWithCompletionHandler(callback)
        }
        return LiveSession(result)
    }
}
