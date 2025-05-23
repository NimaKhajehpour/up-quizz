package com.nima.upquizz.network.models.requests.login

data class LoginRequest(
    var password: String,
    var username: String
){
    fun isValidRequest(): Boolean{
        return password.isNotBlank() && username.isNotBlank()
    }
}