package dev.ynagai.firebase.ai

/**
 * SPIKE(hybrid-inference): [FirebaseAIOnDevice] のオンデバイスモデルの状態。
 * `ONLY_ON_DEVICE`/`PREFER_ON_DEVICE` を呼ぶ前に [checkOnDeviceModelStatus] で確認すること。
 * SDK はモデル未ダウンロード時に自動でダウンロードをトリガーしないため、
 * [DOWNLOADABLE] であれば [downloadOnDeviceModel] を明示的に呼ぶ必要がある。
 */
enum class OnDeviceModelStatus {
    UNAVAILABLE,
    DOWNLOADABLE,
    DOWNLOADING,
    AVAILABLE,
}

/** SPIKE(hybrid-inference): [FirebaseAIOnDevice.download] の進捗イベント。 */
sealed interface OnDeviceModelDownloadStatus {
    data class Started(
        val bytesToDownload: Long,
    ) : OnDeviceModelDownloadStatus

    data class InProgress(
        val totalBytesDownloaded: Long,
    ) : OnDeviceModelDownloadStatus

    data object Completed : OnDeviceModelDownloadStatus

    data class Failed(
        val message: String?,
    ) : OnDeviceModelDownloadStatus
}

/** SPIKE(hybrid-inference): 現在のオンデバイスモデルの状態を確認する。Android のみ実装。 */
expect suspend fun checkOnDeviceModelStatus(): OnDeviceModelStatus

/**
 * SPIKE(hybrid-inference): オンデバイスモデルのダウンロードを開始し、進捗を通知する。
 * [OnDeviceModelStatus.DOWNLOADABLE] のときのみ呼ぶこと。Android のみ実装。
 */
expect fun downloadOnDeviceModel(): kotlinx.coroutines.flow.Flow<OnDeviceModelDownloadStatus>
