package dev.ynagai.firebase.auth

import kotlinx.cinterop.ExperimentalForeignApi
import swiftPMImport.dev.ynagai.firebase.firebase.auth.FIRTOTPSecret

@OptIn(ExperimentalForeignApi::class)
actual class TotpSecret internal constructor(
    internal val apple: FIRTOTPSecret,
) {
    actual val sharedSecretKey: String
        get() = apple.sharedSecretKey()

    actual fun generateQrCodeUrl(accountName: String, issuer: String): String =
        apple.generateQRCodeURLWithAccountName(accountName, issuer = issuer)
}
