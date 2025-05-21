package com.nima.upquizz.network.models.responses

data class ProfileResponse(
    val about: String,
    val display_name: String,
    val id: Int,
    val role: String,
    val username: String
)