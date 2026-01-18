package dev.ynagai.firebase.ai

/**
 * Represents the backend service for generative AI operations.
 *
 * Firebase AI supports two backends:
 * - **Google AI**: Uses Google's consumer AI services directly.
 * - **Vertex AI**: Uses Google Cloud's enterprise AI platform.
 *
 * Use the companion object factory functions to create instances.
 *
 * @sample
 * ```kotlin
 * // Using Google AI (default)
 * val ai = Firebase.ai(backend = GenerativeBackend.googleAI())
 *
 * // Using Vertex AI with a specific location
 * val ai = Firebase.ai(backend = GenerativeBackend.vertexAI("europe-west1"))
 * ```
 */
expect class GenerativeBackend {
    companion object {
        /**
         * Creates a backend configuration for Google AI.
         *
         * Google AI is the default backend and provides access to Google's
         * consumer-facing generative AI services.
         *
         * @return A [GenerativeBackend] configured for Google AI.
         */
        fun googleAI(): GenerativeBackend

        /**
         * Creates a backend configuration for Vertex AI.
         *
         * Vertex AI is Google Cloud's enterprise AI platform, offering
         * additional features like data residency control and enterprise-grade SLAs.
         *
         * @param location The Google Cloud region for the Vertex AI endpoint.
         *                 Defaults to "us-central1".
         * @return A [GenerativeBackend] configured for Vertex AI.
         */
        fun vertexAI(location: String = "us-central1"): GenerativeBackend
    }
}
