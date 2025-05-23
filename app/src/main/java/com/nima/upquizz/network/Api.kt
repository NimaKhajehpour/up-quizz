package com.nima.upquizz.network

import com.nima.upquizz.network.models.requests.login.LoginRequest
import com.nima.upquizz.network.models.requests.signup.UserCreateModel
import com.nima.upquizz.network.models.responses.profile.ProfileResponse
import com.nima.upquizz.network.models.responses.quiz.list.QuizListResponse
import com.nima.upquizz.network.models.responses.token.TokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

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

    @Headers("Content-Type: application/json")
    @GET("user")
    suspend fun getUserProfile(
        @Header("Authorization") token: String
    ): Response<ProfileResponse>

    @Headers("Content-Type: application/json")
    @GET("quiz")
    suspend fun getAllQuizzes(
        @Header("Authorization") token: String,
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 1
    ): Response<QuizListResponse>

    @Headers("Content-Type: application/json")
    @GET("quiz/search")
    suspend fun searchQuizzes(
        @Header("Authorization") token: String,
        @Query("query") query: String,
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 1
    ): Response<QuizListResponse>
}