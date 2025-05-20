package com.nima.upquizz.network

import com.nima.upquizz.network.models.requests.LoginRequest
import com.nima.upquizz.network.models.requests.UserCreateModel
import com.nima.upquizz.network.models.responses.TokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface Api {

    @Headers("accept: application/json", "Content-Type: application/json")
    @POST("register")
    suspend fun register(
        @Body userCreate: UserCreateModel
    ): Response<TokenResponse>

    @Headers("accept: application/json", "Content-Type: application/json")
    @POST("token")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): Response<TokenResponse>
}