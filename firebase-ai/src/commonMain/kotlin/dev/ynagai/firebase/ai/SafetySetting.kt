package dev.ynagai.firebase.ai

/**
 * Configuration for content safety filtering.
 *
 * Use safety settings to control what types of content are blocked
 * in model responses.
 *
 * @property harmCategory The category of harm to configure.
 * @property threshold The blocking threshold for this category.
 *
 * @sample
 * ```kotlin
 * val model = ai.generativeModel(
 *     modelName = "gemini-2.0-flash",
 *     safetySettings = listOf(
 *         SafetySetting(HarmCategory.HARASSMENT, HarmBlockThreshold.BLOCK_MEDIUM_AND_ABOVE),
 *         SafetySetting(HarmCategory.HATE_SPEECH, HarmBlockThreshold.BLOCK_LOW_AND_ABOVE)
 *     )
 * )
 * ```
 */
data class SafetySetting(
    val harmCategory: HarmCategory,
    val threshold: HarmBlockThreshold,
)
