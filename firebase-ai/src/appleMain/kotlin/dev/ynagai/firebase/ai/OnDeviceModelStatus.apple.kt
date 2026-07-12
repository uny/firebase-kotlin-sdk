package dev.ynagai.firebase.ai

import kotlinx.coroutines.flow.flowOf

// TODO(hybrid-inference): wire to `uny/firebase-objc-sdk`'s KFBSystemLanguageModel.availability()
//  once that spike publishes. Until then, always reports UNAVAILABLE / a failed download so
//  callers on Apple platforms fail fast instead of silently hanging.
actual suspend fun checkOnDeviceModelStatus(): OnDeviceModelStatus = OnDeviceModelStatus.UNAVAILABLE

actual fun downloadOnDeviceModel(): kotlinx.coroutines.flow.Flow<OnDeviceModelDownloadStatus> =
    flowOf(OnDeviceModelDownloadStatus.Failed("not implemented on Apple platforms yet (hybrid-inference spike)"))
