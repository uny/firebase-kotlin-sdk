package dev.ynagai.firebase.auth

import com.google.firebase.auth.TotpSecret as AndroidTotpSecret

actual class TotpSecret internal constructor(
    internal val android: AndroidTotpSecret,
) {
    actual val sharedSecretKey: String
        get() = android.sharedSecretKey

    actual fun generateQrCodeUrl(accountName: String, issuer: String): String =
        android.generateQrCodeUrl(accountName, issuer)
}
