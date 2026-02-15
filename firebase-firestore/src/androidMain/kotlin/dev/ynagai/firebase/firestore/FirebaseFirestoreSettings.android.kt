package dev.ynagai.firebase.firestore

import com.google.firebase.firestore.FirebaseFirestoreSettings as AndroidFirebaseFirestoreSettings

@Suppress("DEPRECATION")
internal fun FirebaseFirestoreSettings.toAndroid(): AndroidFirebaseFirestoreSettings =
    AndroidFirebaseFirestoreSettings.Builder()
        .setHost(host)
        .setSslEnabled(isSslEnabled)
        .setPersistenceEnabled(isPersistenceEnabled)
        .setCacheSizeBytes(cacheSizeBytes)
        .build()

@Suppress("DEPRECATION")
internal fun AndroidFirebaseFirestoreSettings.toCommon(): FirebaseFirestoreSettings =
    FirebaseFirestoreSettings(
        host = host,
        isSslEnabled = isSslEnabled,
        isPersistenceEnabled = isPersistenceEnabled,
        cacheSizeBytes = cacheSizeBytes,
    )
