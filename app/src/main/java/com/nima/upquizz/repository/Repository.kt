package com.nima.upquizz.repository

import com.nima.upquizz.network.Api
import com.nima.upquizz.network.models.requests.login.LoginRequest
import com.nima.upquizz.network.models.requests.signup.UserCreateModel

class Repository (private val api: Api) {

    suspend fun register(userCreate: UserCreateModel) = api.register(userCreate)
    suspend fun login(loginRequest: LoginRequest) = api.login(loginRequest)
    suspend fun getProfile(token: String) = api.getUserProfile("Bearer $token")
    suspend fun getAllQuizzes(token: String, page: Int = 1, size: Int = 10) = api.getAllQuizzes("Bearer $token", page = page, size = size)
    suspend fun searchQuizzes(token: String, query: String, page: Int = 1, size: Int = 10) = api.searchQuizzes("Bearer $token", query, page, size)
}