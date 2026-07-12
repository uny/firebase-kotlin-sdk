package dev.ynagai.firebase.ai

import com.google.firebase.ai.InferenceMode as AndroidInferenceMode
import com.google.firebase.ai.OnDeviceConfig as AndroidOnDeviceConfig
import com.google.firebase.ai.OnDeviceModelOption as AndroidOnDeviceModelOption
import com.google.firebase.ai.type.PublicPreviewAPI

@OptIn(PublicPreviewAPI::class)
internal fun OnDeviceConfig.toAndroid(): AndroidOnDeviceConfig = AndroidOnDeviceConfig(
    mode = mode.toAndroid(),
    maxOutputTokens = maxOutputTokens,
    temperature = temperature,
    topK = topK,
    candidateCount = candidateCount,
    modelOption = modelOption?.toAndroid(),
)

@OptIn(PublicPreviewAPI::class)
internal fun InferenceMode.toAndroid(): AndroidInferenceMode = when (this) {
    InferenceMode.PREFER_ON_DEVICE -> AndroidInferenceMode.PREFER_ON_DEVICE
    InferenceMode.ONLY_ON_DEVICE -> AndroidInferenceMode.ONLY_ON_DEVICE
    InferenceMode.PREFER_IN_CLOUD -> AndroidInferenceMode.PREFER_IN_CLOUD
    InferenceMode.ONLY_IN_CLOUD -> AndroidInferenceMode.ONLY_IN_CLOUD
}

@OptIn(PublicPreviewAPI::class)
internal fun OnDeviceModelOption.toAndroid(): AndroidOnDeviceModelOption = when (this) {
    OnDeviceModelOption.STABLE -> AndroidOnDeviceModelOption.STABLE
    OnDeviceModelOption.PREVIEW -> AndroidOnDeviceModelOption.PREVIEW
    OnDeviceModelOption.PREVIEW_FAST -> AndroidOnDeviceModelOption.PREVIEW_FAST
}
