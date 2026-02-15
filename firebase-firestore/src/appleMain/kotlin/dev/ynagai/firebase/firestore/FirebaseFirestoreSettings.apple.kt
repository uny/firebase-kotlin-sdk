package dev.ynagai.firebase.firestore

import kotlinx.cinterop.ExperimentalForeignApi
import swiftPMImport.dev.ynagai.firebase.firebase.firestore.FIRFirestoreSettings

@OptIn(ExperimentalForeignApi::class)
internal fun FirebaseFirestoreSettings.toApple(): FIRFirestoreSettings {
    val settings = FIRFirestoreSettings()
    settings.host = host
    settings.sslEnabled = isSslEnabled
    settings.persistenceEnabled = isPersistenceEnabled
    settings.cacheSizeBytes = cacheSizeBytes
    return settings
}

@OptIn(ExperimentalForeignApi::class)
internal fun FIRFirestoreSettings.toCommon(): FirebaseFirestoreSettings =
    FirebaseFirestoreSettings(
        host = host,
        isSslEnabled = sslEnabled,
        isPersistenceEnabled = persistenceEnabled,
        cacheSizeBytes = cacheSizeBytes,
    )
