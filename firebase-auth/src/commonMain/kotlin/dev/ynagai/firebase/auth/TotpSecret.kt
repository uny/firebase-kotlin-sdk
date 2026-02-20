package dev.ynagai.firebase.auth

expect class TotpSecret {
    val sharedSecretKey: String
    fun generateQrCodeUrl(accountName: String, issuer: String): String
}
