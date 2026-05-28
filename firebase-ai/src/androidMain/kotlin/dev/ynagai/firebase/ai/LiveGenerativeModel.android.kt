@file:OptIn(PublicPreviewAPI::class)

package dev.ynagai.firebase.ai

import com.google.firebase.ai.LiveGenerativeModel as AndroidLiveGenerativeModel
import com.google.firebase.ai.type.PublicPreviewAPI

actual class LiveGenerativeModel internal constructor(
    internal val android: AndroidLiveGenerativeModel,
) {
    actual suspend fun connect(sessionResumption: SessionResumptionConfig?): LiveSession =
        wrapAndroidException {
            if (sessionResumption != null) {
                LiveSession(android.connect(sessionResumption.toAndroid()))
            } else {
                LiveSession(android.connect())
            }
        }
}
