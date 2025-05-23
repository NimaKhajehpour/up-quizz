package com.nima.upquizz.viewmodels

import androidx.lifecycle.ViewModel
import com.nima.upquizz.repository.Repository

class HomeViewModel(private val repository: Repository): ViewModel() {

    suspend fun getAllQuizzes(token: String, page: Int = 1) = repository.getAllQuizzes(token, page)
    suspend fun searchQuizzes(token: String, query: String, page: Int = 1) = repository.searchQuizzes(token, query, page)
}