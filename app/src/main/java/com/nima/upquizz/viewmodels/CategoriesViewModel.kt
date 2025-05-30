package com.nima.upquizz.viewmodels

import androidx.lifecycle.ViewModel
import com.nima.upquizz.repository.Repository

class CategoriesViewModel(private val repository: Repository): ViewModel() {

    suspend fun getAllCategories(token: String, page: Int = 1) = repository.getAllCategories(token, page)
    suspend fun searchCategories(token: String, query: String, page: Int = 1) = repository.searchCategories(token, query, page)
    suspend fun changeCategoryApprove(token: String, id: Int, approved: Boolean) = repository.changeCategoryApprove(token, id, approved)
    suspend fun deleteCategory(token: String, id: Int) = repository.deleteCategory(token, id)

}