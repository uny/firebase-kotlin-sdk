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
    HarmCategory.IMAGE_HATE -> AndroidHarmCategory.IMAGE_HATE
    HarmCategory.IMAGE_DANGEROUS_CONTENT -> AndroidHarmCategory.IMAGE_DANGEROUS_CONTENT
    HarmCategory.IMAGE_HARASSMENT -> AndroidHarmCategory.IMAGE_HARASSMENT
    HarmCategory.IMAGE_SEXUALLY_EXPLICIT -> AndroidHarmCategory.IMAGE_SEXUALLY_EXPLICIT
}

internal fun HarmBlockThreshold.toAndroid(): AndroidHarmBlockThreshold = when (this) {
    HarmBlockThreshold.LOW_AND_ABOVE -> AndroidHarmBlockThreshold.LOW_AND_ABOVE
    HarmBlockThreshold.MEDIUM_AND_ABOVE -> AndroidHarmBlockThreshold.MEDIUM_AND_ABOVE
    HarmBlockThreshold.ONLY_HIGH -> AndroidHarmBlockThreshold.ONLY_HIGH
    HarmBlockThreshold.NONE -> AndroidHarmBlockThreshold.NONE
    HarmBlockThreshold.OFF -> AndroidHarmBlockThreshold.OFF
}
