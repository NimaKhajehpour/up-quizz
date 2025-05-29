package com.nima.upquizz.viewmodels

import androidx.lifecycle.ViewModel
import com.nima.upquizz.repository.Repository

class RateQuizViewModel(private val repository: Repository): ViewModel() {

    suspend fun rateQuiz(token: String, id: Int, rate: Int) = repository.rateQuiz(token, id, rate)
}