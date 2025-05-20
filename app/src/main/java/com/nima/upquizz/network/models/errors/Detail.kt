package com.nima.upquizz.network.models.errors

data class Detail(
    val loc: List<Any>,
    val msg: String,
    val type: String
)