package com.nima.upquizz.network.models.responses.quiz.item

data class Answer(
    val id: Int,
    val isCorrect: Boolean,
    val text: String
)