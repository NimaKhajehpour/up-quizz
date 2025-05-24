package com.nima.upquizz.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.gson.Gson
import com.nima.upquizz.components.QuizListItem
import com.nima.upquizz.components.SearchField
import com.nima.upquizz.network.models.errors.http.HttpError
import com.nima.upquizz.network.models.responses.quiz.list.QuizListResponse
import com.nima.upquizz.viewmodels.HomeViewModel
import kotlinx.coroutines.launch
import retrofit2.Response

@Composable
fun HomeScreen(
    navController: NavController,
    token: String,
    isAdmin: Int,
    viewModel: HomeViewModel
) {

    val scope = rememberCoroutineScope()
    val gson = Gson()

    var query by remember {
        mutableStateOf("")
    }

    var quizzes: Response<QuizListResponse>? by remember {
        mutableStateOf(null)
    }

    var showQuizzes by remember {
        mutableStateOf(false)
    }

    var error: HttpError by remember {
        mutableStateOf(HttpError(detail = ""))
    }

    var retry by remember {
        mutableStateOf(false)
    }

    var toggle by remember {
        mutableStateOf(false)
    }

    var page by remember {
        mutableIntStateOf(1)
    }

    var isSearching by remember {
        mutableStateOf(false)
    }

    LaunchedEffect (toggle){
        if (!isSearching){
            showQuizzes = false
            retry = false
            error = HttpError("")
            try{
                quizzes = viewModel.getAllQuizzes(token, page)
            }catch (e: Exception){
                retry = true
                error = HttpError("Please try again!")
            }
        }
        if (isSearching){
            showQuizzes = false
            retry = false
            error = HttpError("")
            try{
                quizzes = viewModel.searchQuizzes(token, query, page)
            }catch (e: Exception){
                retry = true
                error = HttpError(e.message!!+" Please try again!")
            }
        }
    }

    LaunchedEffect (quizzes){
        if (quizzes != null){
            when(quizzes!!.code()){
                200 ->{
                    retry = false
                    error = HttpError("")
                    showQuizzes = true
                }
                else ->{
                    showQuizzes = false
                    retry = false
                    error = gson.fromJson(quizzes!!.errorBody()!!.string(), HttpError::class.java)
                }
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SearchField(
            query = query,
            onSearch = {
                if (query.isNotBlank()){
                    isSearching = true
                    page = 1
                    toggle = !toggle
                }
            },
            enabled = true,
            onQueryChanged = {
                if (it.isBlank() && isSearching){
                    page = 1
                    isSearching = false
                    toggle = !toggle
                }
                query = it
            },
            label = "Search Quizzes"
        )
        if (!showQuizzes && error.detail.isBlank()){
            CircularProgressIndicator()
        }
        if (error.detail.isNotBlank() && !retry){
            Text(error.detail)
        }
        if (error.detail.isNotBlank() && retry){
            Text(error.detail)
            if (query.isBlank()){
                Button(
                    onClick = {
                        toggle = !toggle
                    }
                ) {
                    Text("Try Again")
                }
            }
        }
        if (showQuizzes){
            LazyColumn (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                items(items = quizzes!!.body()!!.items, key = {
                    it.id
                }){
                    var expanded by remember {
                        mutableStateOf(false)
                    }
                    QuizListItem(
                        isAdmin = isAdmin==1,
                        title = it.title,
                        description = it.description,
                        displayName = "Made by: ${it.user.display_name}",
                        rate = "Rate: ${(it.total_rate/it.rate_count).toInt()}/5",
                        category = "Category: ${it.category.name}",
                        approved = it.approved,
                        onUserClick = {

                        },
                        onRateClick = {

                        },
                        onCategoryClick = {

                        },
                        onAction = {

                        },
                        onExpand = {
                            expanded = !expanded
                        },
                        expanded = expanded
                    ) { }
                }
                if (quizzes!!.body()!!.page != quizzes!!.body()!!.pages){
                    item {
                        Button(
                            onClick = {
                                scope.launch {
                                    page++
                                    toggle = !toggle
                                }
                            }
                        ) {
                            Text("Next Page")
                        }
                    }
                }
            }
        }
    }
}