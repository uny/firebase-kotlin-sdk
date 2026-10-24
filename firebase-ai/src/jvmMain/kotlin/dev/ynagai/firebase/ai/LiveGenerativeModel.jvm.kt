package dev.ynagai.firebase.ai

@Suppress("UNUSED_PARAMETER")
actual class LiveGenerativeModel internal constructor() {
    actual suspend fun connect(sessionResumption: SessionResumptionConfig?): LiveSession =
        unavailableOnJvm()
}
