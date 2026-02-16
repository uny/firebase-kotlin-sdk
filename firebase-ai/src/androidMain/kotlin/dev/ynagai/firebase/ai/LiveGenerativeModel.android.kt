@file:OptIn(PublicPreviewAPI::class)

package dev.ynagai.firebase.ai

import com.google.firebase.ai.LiveGenerativeModel as AndroidLiveGenerativeModel
import com.google.firebase.ai.type.PublicPreviewAPI

actual class LiveGenerativeModel internal constructor(
    internal val android: AndroidLiveGenerativeModel,
) {
    actual suspend fun connect(): LiveSession =
        wrapAndroidException {
            LiveSession(android.connect())
        }
}
