package dev.ynagai.firebase.ai

/**
 * Stable, machine-readable tags for [FirebaseAIException.errorType].
 *
 * Using constants here keeps the platform mappers and consumer code in sync; drift
 * in the raw strings would silently break Sentry grouping and `when` branches.
 */
object FirebaseAIErrorType {
    const val INTERNAL = "internalError"
    const val PROMPT_IMAGE_CONTENT = "promptImageContentError"
    const val PROMPT_BLOCKED = "promptBlocked"
    const val RESPONSE_STOPPED_EARLY = "responseStoppedEarly"
    const val SERVER = "serverError"
    const val INVALID_API_KEY = "invalidApiKey"
    const val QUOTA_EXCEEDED = "quotaExceeded"
    const val UNKNOWN = "unknown"
}

/**
 * Base exception for Firebase AI errors.
 *
 * @property httpStatusCode HTTP status code from the backend, if available.
 * @property responseBody Raw HTTP response body from the backend, if available.
 *   May include prompt echoes; callers are responsible for sanitization before logging.
 * @property errorType Short machine-readable type tag. See [FirebaseAIErrorType].
 * @property underlyingDomain Native error domain (iOS NSError.domain / Android class name).
 * @property underlyingCode Native error code.
 */
open class FirebaseAIException(
    message: String? = null,
    cause: Throwable? = null,
    val httpStatusCode: Int? = null,
    val responseBody: String? = null,
    val errorType: String? = null,
    val underlyingDomain: String? = null,
    val underlyingCode: Int? = null,
) : Exception(message, cause)

/**
 * Exception thrown when a generative AI operation fails.
 */
class GenerativeAIException(
    message: String? = null,
    cause: Throwable? = null,
    httpStatusCode: Int? = null,
    responseBody: String? = null,
    errorType: String? = null,
    underlyingDomain: String? = null,
    underlyingCode: Int? = null,
) : FirebaseAIException(
    message, cause,
    httpStatusCode = httpStatusCode,
    responseBody = responseBody,
    errorType = errorType,
    underlyingDomain = underlyingDomain,
    underlyingCode = underlyingCode,
)

/**
 * Exception thrown when a prompt is blocked by the model.
 */
class PromptBlockedException(
    message: String? = null,
    val response: GenerateContentResponse? = null,
    httpStatusCode: Int? = null,
    responseBody: String? = null,
    underlyingDomain: String? = null,
    underlyingCode: Int? = null,
) : FirebaseAIException(
    message,
    httpStatusCode = httpStatusCode,
    responseBody = responseBody,
    errorType = FirebaseAIErrorType.PROMPT_BLOCKED,
    underlyingDomain = underlyingDomain,
    underlyingCode = underlyingCode,
)

/**
 * Exception thrown when a response is stopped by the model.
 *
 * @property finishReason Reason the model stopped generating, if known.
 */
class ResponseStoppedException(
    message: String? = null,
    val response: GenerateContentResponse? = null,
    val finishReason: String? = null,
    httpStatusCode: Int? = null,
    responseBody: String? = null,
    underlyingDomain: String? = null,
    underlyingCode: Int? = null,
) : FirebaseAIException(
    message,
    httpStatusCode = httpStatusCode,
    responseBody = responseBody,
    errorType = FirebaseAIErrorType.RESPONSE_STOPPED_EARLY,
    underlyingDomain = underlyingDomain,
    underlyingCode = underlyingCode,
)

/**
 * Exception thrown when the API key is invalid.
 */
class InvalidAPIKeyException(
    message: String? = null,
    cause: Throwable? = null,
    httpStatusCode: Int? = null,
    responseBody: String? = null,
    underlyingDomain: String? = null,
    underlyingCode: Int? = null,
) : FirebaseAIException(
    message,
    cause,
    httpStatusCode = httpStatusCode,
    responseBody = responseBody,
    errorType = FirebaseAIErrorType.INVALID_API_KEY,
    underlyingDomain = underlyingDomain,
    underlyingCode = underlyingCode,
)

/**
 * Exception thrown when the API quota is exceeded.
 */
class QuotaExceededException(
    message: String? = null,
    cause: Throwable? = null,
    httpStatusCode: Int? = null,
    responseBody: String? = null,
    underlyingDomain: String? = null,
    underlyingCode: Int? = null,
) : FirebaseAIException(
    message,
    cause,
    httpStatusCode = httpStatusCode,
    responseBody = responseBody,
    errorType = FirebaseAIErrorType.QUOTA_EXCEEDED,
    underlyingDomain = underlyingDomain,
    underlyingCode = underlyingCode,
)

/**
 * Exception thrown when a server error occurs.
 */
class ServerException(
    message: String? = null,
    cause: Throwable? = null,
    httpStatusCode: Int? = null,
    responseBody: String? = null,
    underlyingDomain: String? = null,
    underlyingCode: Int? = null,
) : FirebaseAIException(
    message, cause,
    httpStatusCode = httpStatusCode,
    responseBody = responseBody,
    errorType = FirebaseAIErrorType.SERVER,
    underlyingDomain = underlyingDomain,
    underlyingCode = underlyingCode,
)
