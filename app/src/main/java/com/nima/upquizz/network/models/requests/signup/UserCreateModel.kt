package com.nima.upquizz.network.models.requests.signup

data class UserCreateModel(
    val display_name: String,
    val username: String,
    val about: String? = null,
    val password: String,
){
    fun isValid(): Boolean{
        return display_name.isNotBlank() && username.isNotBlank() && password.isNotBlank()
    }
}
