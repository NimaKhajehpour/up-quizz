package com.nima.upquizz.network.models.requests

data class LoginRequest(
    var password: String,
    var username: String
){
    fun isValidRequest(): Boolean{
        return password.isNotBlank() && username.isNotBlank()
    }
}