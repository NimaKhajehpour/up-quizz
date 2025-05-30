package com.nima.upquizz.viewmodels

import androidx.lifecycle.ViewModel
import com.nima.upquizz.repository.Repository

class CategoryViewModel(private val repository: Repository): ViewModel() {

    suspend fun filterQuizzes(token: String, id: Int, page: Int = 1) = repository.filterQuizzes(token = token, page = page, id = id)
    suspend fun changeQuizApprove(token: String, id: Int, approve: Boolean) = repository.changeQuizApprove(token, id, approve)
}