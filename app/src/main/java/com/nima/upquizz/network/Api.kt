package com.nima.upquizz.network

import com.nima.upquizz.network.models.requests.TakenQuizRequest
import com.nima.upquizz.network.models.requests.login.LoginRequest
import com.nima.upquizz.network.models.requests.signup.UserCreateModel
import com.nima.upquizz.network.models.responses.category.list.CategoryListResponse
import com.nima.upquizz.network.models.responses.profile.ProfileResponse
import com.nima.upquizz.network.models.responses.quiz.filter.QuizFilterResponse
import com.nima.upquizz.network.models.responses.quiz.item.QuizResponse
import com.nima.upquizz.network.models.responses.quiz.list.QuizListResponse
import com.nima.upquizz.network.models.responses.token.TokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
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

    @Headers("Content-Type: application/json", "accept: application/json")
    @PUT("quiz/approve/{id}")
    suspend fun changeQuizApprove(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Query("approved") approved: Boolean
    ): Response<Any>

    @Headers("Content-Type: application/json", "accept: application/json")
    @GET("quiz/{id}")
    suspend fun getQuizById(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<QuizResponse>

    @Headers("Content-Type: application/json", "accept: application/json")
    @POST("taken_quiz")
    suspend fun addTakenQuiz(
        @Header("Authorization") token: String,
        @Body takenQuiz: TakenQuizRequest
    ): Response<Any>

    @Headers("Content-Type: application/json", "accept: application/json")
    @PUT("quiz/rate/{id}")
    suspend fun rateQuiz(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Query("rate") rate: Int
    ): Response<Any>

    @Headers("Content-Type: application/json", "accept: application/json")
    @GET("quiz/filter")
    suspend fun filterQuizzes(
        @Header("Authorization") token: String,
        @Query("category_id") id: Int,
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 1
    ): Response<QuizFilterResponse>

    @Headers("Content-Type: application/json")
    @GET("category")
    suspend fun getAllCategories(
        @Header("Authorization") token: String,
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 1
    ): Response<CategoryListResponse>

    @Headers("Content-Type: application/json")
    @GET("category/search")
    suspend fun searchCategories(
        @Header("Authorization") token: String,
        @Query("query") query: String,
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 1
    ): Response<CategoryListResponse>

    @Headers("Content-Type: application/json", "accept: application/json")
    @PUT("category/approve/{id}")
    suspend fun changeCategoryApprove(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Query("approved") approved: Boolean
    ): Response<Any>

    @Headers("Content-Type: application/json", "accept: application/json")
    @DELETE("category/{id}")
    suspend fun deleteCategory(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
    ): Response<Any>
}