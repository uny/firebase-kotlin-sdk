package dev.ynagai.firebase.ai

/**
 * Base exception for Firebase AI errors.
 */
open class FirebaseAIException(
    message: String? = null,
    cause: Throwable? = null,
) : Exception(message, cause)

/**
 * Exception thrown when a generative AI operation fails.
 */
class GenerativeAIException(
    message: String? = null,
    cause: Throwable? = null,
) : FirebaseAIException(message, cause)

/**
 * Exception thrown when a prompt is blocked by the model.
 *
 * @property response The response that contains the blocking information.
 */
class PromptBlockedException(
    message: String? = null,
    val response: GenerateContentResponse? = null,
) : FirebaseAIException(message)

/**
 * Exception thrown when a response is stopped by the model.
 *
 * @property response The response that was stopped.
 */
class ResponseStoppedException(
    message: String? = null,
    val response: GenerateContentResponse? = null,
) : FirebaseAIException(message)

/**
 * Exception thrown when the API key is invalid.
 */
class InvalidAPIKeyException(
    message: String? = null,
    cause: Throwable? = null,
) : FirebaseAIException(message, cause)

/**
 * Exception thrown when the API quota is exceeded.
 */
class QuotaExceededException(
    message: String? = null,
    cause: Throwable? = null,
) : FirebaseAIException(message, cause)

/**
 * Exception thrown when a server error occurs.
 */
class ServerException(
    message: String? = null,
    cause: Throwable? = null,
) : FirebaseAIException(message, cause)
