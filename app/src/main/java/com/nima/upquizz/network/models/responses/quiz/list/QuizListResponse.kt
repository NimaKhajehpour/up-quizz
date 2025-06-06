package com.nima.upquizz.network.models.responses.quiz.list

import androidx.compose.runtime.snapshots.SnapshotStateList

data class QuizListResponse(
    val items: SnapshotStateList<Item>,
    val page: Int,
    val pages: Int,
    val size: Int,
    val total: Int
)