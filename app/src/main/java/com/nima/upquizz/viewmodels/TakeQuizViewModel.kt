package com.nima.upquizz.viewmodels

import androidx.lifecycle.ViewModel
import com.nima.upquizz.network.models.requests.TakenQuizRequest
import com.nima.upquizz.repository.Repository

class TakeQuizViewModel(private val repository: Repository): ViewModel() {

    suspend fun getQuizById(token: String, id: Int) = repository.getQuizById(token, id)
    suspend fun addTakenQuiz(token: String, takenQuiz: TakenQuizRequest) = repository.addTakenQuiz(token, takenQuiz)
}