package dev.ynagai.firebase.ai

/**
 * Configuration for the session resumption mechanism.
 *
 * @property handle The session resumption handle of the previous session to restore.
 */
data class SessionResumptionConfig(
    val handle: String? = null,
)
