package com.nima.upquizz.network.models.responses.quiz.list

data class Item(
    val approved: Boolean,
    val category: Category,
    val category_id: Int,
    val description: String,
    val id: Int,
    val rate_count: Int,
    val title: String,
    val total_rate: Double,
    val user: User,
    val user_id: Int
)