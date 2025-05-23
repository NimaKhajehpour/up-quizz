package com.nima.upquizz.network.models.responses.token

data class TokenResponse(
    val access_token: String,
    val token_type: String
)