package com.nima.upquizz.repository

import com.nima.upquizz.network.Api
import com.nima.upquizz.network.models.requests.LoginRequest
import com.nima.upquizz.network.models.requests.UserCreateModel

class Repository (private val api: Api) {

    suspend fun register(userCreate: UserCreateModel) = api.register(userCreate)
    suspend fun login(loginRequest: LoginRequest) = api.login(loginRequest)
}