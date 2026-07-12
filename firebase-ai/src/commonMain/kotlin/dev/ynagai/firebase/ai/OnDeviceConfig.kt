package dev.ynagai.firebase.ai

/**
 * Configuration for on-device inference, used by
 * [Hybrid Inference](https://firebase.google.com/docs/ai-logic/hybrid/android/get-started).
 *
 * **Experimental**: Hybrid Inference is an experimental, no-SLA feature of Firebase AI Logic.
 * On Android it requires AICore and a Gemini Nano-capable device (minSdk 26+); the app must also
 * pull in the `firebase-ai-ondevice` (beta) artifact. The API surface may change in a future
 * release.
 *
 * **iOS**: not yet implemented — passing a non-`null` [OnDeviceConfig] to
 * [FirebaseAI.generativeModel] on Apple targets currently has no effect and falls back to
 * in-cloud inference, pending upstream support in the Firebase iOS SDK.
 *
 * @property mode Selects between on-device and in-cloud inference. See [InferenceMode].
 * @property maxOutputTokens The maximum number of tokens to generate in the response. See
 * [GenerationConfig] for more detail.
 * @property temperature A parameter controlling the degree of randomness in token selection. See
 * [GenerationConfig] for more detail.
 * @property topK The `topK` parameter changes how the model selects tokens for output. See
 * [GenerationConfig] for more detail.
 * @property candidateCount The number of generated responses to return. Defaults to `1`.
 * @property modelOption Selects the on-device model variant. See [OnDeviceModelOption].
 */
data class OnDeviceConfig(
    val mode: InferenceMode,
    val maxOutputTokens: Int? = null,
    val temperature: Float? = null,
    val topK: Int? = null,
    val candidateCount: Int = 1,
    val modelOption: OnDeviceModelOption? = null,
) {
    companion object {
        /** Convenience config that disables Hybrid Inference and only uses in-cloud inference. */
        val IN_CLOUD: OnDeviceConfig = OnDeviceConfig(InferenceMode.ONLY_IN_CLOUD)
    }
}
