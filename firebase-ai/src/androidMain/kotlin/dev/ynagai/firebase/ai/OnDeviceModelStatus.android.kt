package dev.ynagai.firebase.ai

import com.google.firebase.ai.ondevice.DownloadStatus as AndroidDownloadStatus
import com.google.firebase.ai.ondevice.FirebaseAIOnDevice
import com.google.firebase.ai.ondevice.OnDeviceModelStatus as AndroidOnDeviceModelStatus
import com.google.firebase.ai.type.PublicPreviewAPI
import kotlinx.coroutines.flow.map

@OptIn(PublicPreviewAPI::class)
actual suspend fun checkOnDeviceModelStatus(): OnDeviceModelStatus =
    when (FirebaseAIOnDevice.checkStatus()) {
        AndroidOnDeviceModelStatus.UNAVAILABLE -> OnDeviceModelStatus.UNAVAILABLE
        AndroidOnDeviceModelStatus.DOWNLOADABLE -> OnDeviceModelStatus.DOWNLOADABLE
        AndroidOnDeviceModelStatus.DOWNLOADING -> OnDeviceModelStatus.DOWNLOADING
        AndroidOnDeviceModelStatus.AVAILABLE -> OnDeviceModelStatus.AVAILABLE
        else -> OnDeviceModelStatus.UNAVAILABLE
    }

@OptIn(PublicPreviewAPI::class)
actual fun downloadOnDeviceModel(): kotlinx.coroutines.flow.Flow<OnDeviceModelDownloadStatus> =
    FirebaseAIOnDevice.download().map { status ->
        when (status) {
            is AndroidDownloadStatus.DownloadStarted ->
                OnDeviceModelDownloadStatus.Started(status.bytesToDownload)

            is AndroidDownloadStatus.DownloadInProgress ->
                OnDeviceModelDownloadStatus.InProgress(status.totalBytesDownloaded)

            is AndroidDownloadStatus.DownloadCompleted ->
                OnDeviceModelDownloadStatus.Completed

            is AndroidDownloadStatus.DownloadFailed ->
                OnDeviceModelDownloadStatus.Failed(status.toString())

            else -> OnDeviceModelDownloadStatus.Failed("unknown status: $status")
        }
    }
