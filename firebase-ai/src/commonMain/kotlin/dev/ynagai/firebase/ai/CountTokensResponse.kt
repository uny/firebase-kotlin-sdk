package dev.ynagai.firebase.ai

data class CountTokensResponse(
    val totalTokens: Int,
    val totalBillableCharacters: Int? = null,
)
