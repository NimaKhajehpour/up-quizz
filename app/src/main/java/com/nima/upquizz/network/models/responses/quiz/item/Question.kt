package com.nima.upquizz.network.models.responses.quiz.item

data class Question(
    val answers: List<Answer>,
    val id: Int,
    val text: String
)