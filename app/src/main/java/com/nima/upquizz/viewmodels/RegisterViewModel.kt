package com.nima.upquizz.viewmodels

import androidx.lifecycle.ViewModel
import com.nima.upquizz.network.models.requests.signup.UserCreateModel
import com.nima.upquizz.repository.Repository

class RegisterViewModel(private val repository: Repository): ViewModel() {

    suspend fun register(userCreate: UserCreateModel) = repository.register(userCreate)
}