package com.nima.upquizz.viewmodels

import androidx.lifecycle.ViewModel
import com.nima.upquizz.repository.Repository

class MainViewModel(private val repository: Repository): ViewModel() {

    suspend fun getProfile(token: String) = repository.getProfile(token)
}