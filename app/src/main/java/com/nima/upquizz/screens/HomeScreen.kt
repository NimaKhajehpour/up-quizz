package com.nima.upquizz.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.navArgument
import com.google.gson.Gson
import com.nima.upquizz.components.QuizListItem
import com.nima.upquizz.components.SearchField
import com.nima.upquizz.navigation.pages.PagesScreens
import com.nima.upquizz.network.models.errors.http.HttpError
import com.nima.upquizz.network.models.responses.quiz.list.QuizListResponse
import com.nima.upquizz.viewmodels.HomeViewModel
import kotlinx.coroutines.launch
import retrofit2.Response

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    token: String,
    isAdmin: Int,
    viewModel: HomeViewModel
) {

    val scope = rememberCoroutineScope()
    val gson = Gson()
    val context = LocalContext.current

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

    var isRefreshing by remember {
        mutableStateOf(false)
    }

    LaunchedEffect (toggle){
        if (!isSearching){
            error = HttpError("")
            showQuizzes = false
            retry = false
            try{
                quizzes = viewModel.getAllQuizzes(token, page)
                isRefreshing = false
            }catch (e: Exception){
                retry = true
                error = HttpError("Please try again!")
            }
        }
        if (isSearching){
            error = HttpError("")
            showQuizzes = false
            retry = false
            try{
                quizzes = viewModel.searchQuizzes(token, query, page)
                isRefreshing = false
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

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = {
            isRefreshing = true
            toggle = !toggle
        }

    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SearchField(
                query = query,
                onSearch = {
                    if (query.isNotBlank()) {
                        isSearching = true
                        page = 1
                        toggle = !toggle
                    }
                },
                enabled = true,
                onQueryChanged = {
                    if (it.isBlank() && isSearching) {
                        page = 1
                        isSearching = false
                        toggle = !toggle
                    }
                    query = it
                },
                label = "Search Quizzes"
            )
            if (!showQuizzes && error.detail.isBlank()) {
                Column (
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    CircularProgressIndicator()
                }
            }
            if (error.detail.isNotBlank() && !retry) {
                Text(error.detail)
            }
            if (error.detail.isNotBlank() && retry) {
                Text(error.detail)
                Button(
                    onClick = {
                        toggle = !toggle
                    }
                ) {
                    Text("Try Again")
                }
            }
            if (showQuizzes) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    itemsIndexed(items = quizzes!!.body()!!.items) { index, it ->
                        var expanded by remember {
                            mutableStateOf(false)
                        }
                        QuizListItem(
                            isAdmin = isAdmin == 1,
                            title = it.title,
                            description = it.description,
                            displayName = "Made by: ${it.user.display_name}",
                            rate = "Rate: ${(it.total_rate / it.rate_count).takeIf { !it.isNaN() } ?: 0}/5",
                            category = "Category: ${it.category.name}",
                            approved = it.approved,
                            onUserClick = {
                                //todo add user click actions
                            },
                            onCategoryClick = {
                                navController.navigate(PagesScreens.CategoryScreen.name+"/${it.category.id}/${it.category.name}"){
                                    popUpTo(PagesScreens.HomeScreen.name){
                                        inclusive = true
                                    }
                                    launchSingleTop = true
                                }
                            },
                            onAction = {
                                scope.launch {
                                    try {
                                        viewModel.changeQuizApprove(token, it.id, !it.approved)
                                            .apply {
                                                if (isSuccessful) {
                                                    expanded = false
                                                    Toast.makeText(
                                                        context,
                                                        "Approve status changed",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    quizzes!!.body()!!.items[index] =
                                                        it.copy(approved = !it.approved)
                                                } else {
                                                    Toast.makeText(
                                                        context,
                                                        "An error occurred",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                            }
                                    } catch (e: Exception) {
                                        Toast.makeText(
                                            context,
                                            "An error occurred",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            },
                            onExpand = {
                                expanded = !expanded
                            },
                            expanded = expanded
                        ) {
                            navController.navigate(PagesScreens.TakeQuizScreen.name + "/${it.id}") {
                                popUpTo(PagesScreens.HomeScreen.name) {
                                    inclusive = true
                                }
                                launchSingleTop = true
                            }
                        }
                    }
                    if (quizzes!!.body()!!.page != quizzes!!.body()!!.pages) {
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
                    if (quizzes!!.body()!!.page > 1) {
                        item {
                            Button(
                                onClick = {
                                    scope.launch {
                                        page--
                                        toggle = !toggle
                                    }
                                }
                            ) {
                                Text("Previous Page")
                            }
                        }
                    }
                }
            }
        }
    }
}