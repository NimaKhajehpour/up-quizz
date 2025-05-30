package com.nima.upquizz.network.models.responses.category.list

import androidx.compose.runtime.snapshots.SnapshotStateList

data class CategoryListResponse(
    val items: SnapshotStateList<Item>,
    val page: Int,
    val pages: Int,
    val size: Int,
    val total: Int
)