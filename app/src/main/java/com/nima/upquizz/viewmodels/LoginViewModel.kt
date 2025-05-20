package com.nima.upquizz.viewmodels

import androidx.lifecycle.ViewModel
import com.nima.upquizz.network.models.requests.LoginRequest
import com.nima.upquizz.repository.Repository

class LoginViewModel(private val repository: Repository): ViewModel() {

    suspend fun login(loginRequest: LoginRequest) = repository.login(loginRequest)
}