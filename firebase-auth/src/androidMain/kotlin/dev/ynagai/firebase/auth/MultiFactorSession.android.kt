package dev.ynagai.firebase.auth

import com.google.firebase.auth.MultiFactorSession as AndroidMultiFactorSession

actual class MultiFactorSession internal constructor(
    internal val android: AndroidMultiFactorSession,
)
