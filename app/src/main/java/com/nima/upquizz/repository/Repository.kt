package com.nima.upquizz.repository

import com.nima.upquizz.network.Api
import com.nima.upquizz.network.models.requests.TakenQuizRequest
import com.nima.upquizz.network.models.requests.login.LoginRequest
import com.nima.upquizz.network.models.requests.signup.UserCreateModel

class Repository (private val api: Api) {

    suspend fun register(userCreate: UserCreateModel) = api.register(userCreate)
    suspend fun login(loginRequest: LoginRequest) = api.login(loginRequest)
    suspend fun getProfile(token: String) = api.getUserProfile("Bearer $token")
    suspend fun getAllQuizzes(token: String, page: Int = 1, size: Int = 10) = api.getAllQuizzes("Bearer $token", page = page, size = size)
    suspend fun filterQuizzes(token: String, page: Int = 1, size: Int = 10, id: Int) = api.filterQuizzes("Bearer $token", page = page, size = size, id = id)
    suspend fun searchQuizzes(token: String, query: String, page: Int = 1, size: Int = 10) = api.searchQuizzes("Bearer $token", query, page, size)
    suspend fun changeQuizApprove(token: String, id: Int, approved: Boolean) = api.changeQuizApprove(token = "Bearer $token", id = id, approved = approved)
    suspend fun getQuizById(token: String, id: Int) = api.getQuizById("Bearer $token", id)
    suspend fun addTakenQuiz(token: String, takenQuiz: TakenQuizRequest) = api.addTakenQuiz("Bearer $token", takenQuiz)
    suspend fun rateQuiz(token: String, id: Int, rate: Int) = api.rateQuiz("Bearer $token", id, rate)
    suspend fun getAllCategories(token: String, page: Int = 1, size: Int = 10) = api.getAllCategories("Bearer $token", page = page, size = size)
    suspend fun searchCategories(token: String, query: String, page: Int = 1, size: Int = 10) = api.searchCategories("Bearer $token", query, page, size)
    suspend fun changeCategoryApprove(token: String, id: Int, approved: Boolean) = api.changeCategoryApprove(token = "Bearer $token", id = id, approved = approved)
    suspend fun deleteCategory(token: String, id: Int) = api.deleteCategory(token = "Bearer $token", id = id)

}