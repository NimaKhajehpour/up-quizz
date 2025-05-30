package com.nima.upquizz.network.models.responses.category.list

data class Item(
    val approved: Boolean,
    val description: String,
    val id: Int,
    val name: String
)