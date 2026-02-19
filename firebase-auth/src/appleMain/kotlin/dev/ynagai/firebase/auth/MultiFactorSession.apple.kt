package dev.ynagai.firebase.auth

import kotlinx.cinterop.ExperimentalForeignApi
import swiftPMImport.dev.ynagai.firebase.firebase.auth.FIRMultiFactorSession

@OptIn(ExperimentalForeignApi::class)
actual class MultiFactorSession internal constructor(
    internal val apple: FIRMultiFactorSession,
)
