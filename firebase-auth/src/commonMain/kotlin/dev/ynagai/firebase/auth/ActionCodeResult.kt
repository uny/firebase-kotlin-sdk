package dev.ynagai.firebase.auth

enum class ActionCodeOperation {
    PASSWORD_RESET,
    VERIFY_EMAIL,
    RECOVER_EMAIL,
    SIGN_IN_WITH_EMAIL_LINK,
    VERIFY_AND_CHANGE_EMAIL,
    REVERT_SECOND_FACTOR_ADDITION,
    UNKNOWN,
}

data class ActionCodeResult(
    val operation: ActionCodeOperation,
    val email: String? = null,
    val previousEmail: String? = null,
)
