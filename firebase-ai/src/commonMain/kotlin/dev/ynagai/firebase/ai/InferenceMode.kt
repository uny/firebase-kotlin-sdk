package dev.ynagai.firebase.ai

/**
 * Selects between on-device and in-cloud inference for
 * [Hybrid Inference](https://firebase.google.com/docs/ai-logic/hybrid/android/get-started).
 *
 * **Experimental**: Hybrid Inference is an experimental, no-SLA feature of Firebase AI Logic.
 * On Android it requires AICore and a Gemini Nano-capable device (minSdk 26+). The API surface
 * may change in a future release.
 *
 * Currently only implemented on Android. See [OnDeviceConfig].
 */
enum class InferenceMode {
    /**
     * Prefer on-device inference, but fall back to in-cloud inference if the on-device model is
     * unavailable or unable to generate an answer.
     */
    PREFER_ON_DEVICE,

    /** Only use on-device inference; no in-cloud fallback. */
    ONLY_ON_DEVICE,

    /**
     * Prefer in-cloud inference, but fall back to on-device inference if the device is offline.
     */
    PREFER_IN_CLOUD,

    /** Only use in-cloud inference; no on-device fallback. */
    ONLY_IN_CLOUD,
}

/**
 * Selects which on-device model variant to use for [Hybrid Inference][InferenceMode].
 *
 * **Experimental**: See [InferenceMode] for details on Hybrid Inference's experimental status.
 */
enum class OnDeviceModelOption {
    /** The stable, fully-featured on-device model. */
    STABLE,

    /** A preview build of the on-device model. */
    PREVIEW,

    /** A preview build of the on-device model, optimized for lower latency. */
    PREVIEW_FAST,
}
