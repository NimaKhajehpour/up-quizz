package com.nima.upquizz.network.models.responses

data class TokenResponse(
    val access_token: String,
    val token_type: String
)