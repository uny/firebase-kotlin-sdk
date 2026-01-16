package dev.ynagai.firebase.ai

import com.google.firebase.ai.type.HarmBlockThreshold as AndroidHarmBlockThreshold
import com.google.firebase.ai.type.HarmCategory as AndroidHarmCategory
import com.google.firebase.ai.type.SafetySetting as AndroidSafetySetting

internal fun SafetySetting.toAndroid(): AndroidSafetySetting = AndroidSafetySetting(
    harmCategory = harmCategory.toAndroid(),
    threshold = threshold.toAndroid(),
)

internal fun HarmCategory.toAndroid(): AndroidHarmCategory = when (this) {
    HarmCategory.UNKNOWN -> AndroidHarmCategory.UNKNOWN
    HarmCategory.HARASSMENT -> AndroidHarmCategory.HARASSMENT
    HarmCategory.HATE_SPEECH -> AndroidHarmCategory.HATE_SPEECH
    HarmCategory.SEXUALLY_EXPLICIT -> AndroidHarmCategory.SEXUALLY_EXPLICIT
    HarmCategory.DANGEROUS_CONTENT -> AndroidHarmCategory.DANGEROUS_CONTENT
    HarmCategory.CIVIC_INTEGRITY -> AndroidHarmCategory.CIVIC_INTEGRITY
}

internal fun HarmBlockThreshold.toAndroid(): AndroidHarmBlockThreshold = when (this) {
    HarmBlockThreshold.UNSPECIFIED -> AndroidHarmBlockThreshold.OFF
    HarmBlockThreshold.BLOCK_LOW_AND_ABOVE -> AndroidHarmBlockThreshold.LOW_AND_ABOVE
    HarmBlockThreshold.BLOCK_MEDIUM_AND_ABOVE -> AndroidHarmBlockThreshold.MEDIUM_AND_ABOVE
    HarmBlockThreshold.BLOCK_ONLY_HIGH -> AndroidHarmBlockThreshold.ONLY_HIGH
    HarmBlockThreshold.BLOCK_NONE -> AndroidHarmBlockThreshold.OFF
}
