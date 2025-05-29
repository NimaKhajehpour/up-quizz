package com.nima.upquizz.network.models.responses.quiz.list

data class QuizListResponse(
    val items: MutableList<Item>,
    val page: Int,
    val pages: Int,
    val size: Int,
    val total: Int
)