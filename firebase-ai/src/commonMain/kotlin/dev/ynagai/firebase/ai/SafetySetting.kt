package dev.ynagai.firebase.ai

data class SafetySetting(
    val harmCategory: HarmCategory,
    val threshold: HarmBlockThreshold,
)
