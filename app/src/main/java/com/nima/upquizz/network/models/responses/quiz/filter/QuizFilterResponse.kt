package com.nima.upquizz.network.models.responses.quiz.filter

import androidx.compose.runtime.snapshots.SnapshotStateList

data class QuizFilterResponse(
    val items: SnapshotStateList<Item>,
    val page: Int,
    val pages: Int,
    val size: Int,
    val total: Int
)