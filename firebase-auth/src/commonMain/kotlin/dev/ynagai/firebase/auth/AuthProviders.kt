package dev.ynagai.firebase.auth

expect object EmailAuthProvider {
    fun credential(email: String, password: String): AuthCredential
}

expect object GoogleAuthProvider {
    fun credential(idToken: String?, accessToken: String?): AuthCredential
}

expect object PhoneAuthProvider {
    fun credential(verificationId: String, smsCode: String): AuthCredential
}

expect object OAuthProvider {
    fun credential(
        providerId: String,
        idToken: String? = null,
        accessToken: String? = null,
    ): AuthCredential
}
