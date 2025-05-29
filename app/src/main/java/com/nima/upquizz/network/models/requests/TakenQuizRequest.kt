package com.nima.upquizz.network.models.requests

data class TakenQuizRequest(
    val correct_answers: Int,
    val quiz_id: Int,
    val total_answers: Int
)