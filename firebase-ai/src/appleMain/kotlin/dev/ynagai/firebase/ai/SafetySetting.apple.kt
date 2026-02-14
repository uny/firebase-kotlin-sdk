package dev.ynagai.firebase.ai

import kotlinx.cinterop.ExperimentalForeignApi
import swiftPMImport.dev.ynagai.firebase.firebase.ai.KFBHarmBlockThreshold
import swiftPMImport.dev.ynagai.firebase.firebase.ai.KFBHarmCategory
import swiftPMImport.dev.ynagai.firebase.firebase.ai.KFBSafetySetting

@OptIn(ExperimentalForeignApi::class)
internal fun SafetySetting.toApple(): KFBSafetySetting = KFBSafetySetting(
    harmCategory = harmCategory.toApple(),
    threshold = threshold.toApple(),
)

@OptIn(ExperimentalForeignApi::class)
internal fun HarmCategory.toApple(): KFBHarmCategory = when (this) {
    HarmCategory.HARASSMENT -> KFBHarmCategory.harassment()
    HarmCategory.HATE_SPEECH -> KFBHarmCategory.hateSpeech()
    HarmCategory.SEXUALLY_EXPLICIT -> KFBHarmCategory.sexuallyExplicit()
    HarmCategory.DANGEROUS_CONTENT -> KFBHarmCategory.dangerousContent()
    HarmCategory.CIVIC_INTEGRITY -> KFBHarmCategory.civicIntegrity()
    // No direct iOS equivalents for these; fall back to harassment
    HarmCategory.UNKNOWN,
    HarmCategory.IMAGE_HATE,
    HarmCategory.IMAGE_DANGEROUS_CONTENT,
    HarmCategory.IMAGE_HARASSMENT,
    HarmCategory.IMAGE_SEXUALLY_EXPLICIT -> KFBHarmCategory.harassment()
}

@OptIn(ExperimentalForeignApi::class)
internal fun HarmBlockThreshold.toApple(): KFBHarmBlockThreshold = when (this) {
    HarmBlockThreshold.LOW_AND_ABOVE -> KFBHarmBlockThreshold.blockLowAndAbove()
    HarmBlockThreshold.MEDIUM_AND_ABOVE -> KFBHarmBlockThreshold.blockMediumAndAbove()
    HarmBlockThreshold.ONLY_HIGH -> KFBHarmBlockThreshold.blockOnlyHigh()
    HarmBlockThreshold.NONE -> KFBHarmBlockThreshold.blockNone()
    HarmBlockThreshold.OFF -> KFBHarmBlockThreshold.off()
}
