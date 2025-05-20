package com.nima.upquizz.network.models.requests

data class UserCreateModel(
    val display_name: String,
    val username: String,
    val about: String = "",
    val password: String,
){
    fun isValid(): Boolean{
        return display_name.isNotBlank() && username.isNotBlank() && password.isNotBlank()
    }
}
