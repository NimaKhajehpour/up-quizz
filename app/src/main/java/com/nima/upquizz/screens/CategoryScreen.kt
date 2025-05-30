package com.nima.upquizz.screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.gson.Gson
import com.nima.upquizz.components.QuizListItem
import com.nima.upquizz.components.SearchField
import com.nima.upquizz.navigation.pages.PagesScreens
import com.nima.upquizz.network.models.errors.http.HttpError
import com.nima.upquizz.network.models.responses.quiz.filter.QuizFilterResponse
import com.nima.upquizz.network.models.responses.quiz.list.QuizListResponse
import com.nima.upquizz.viewmodels.CategoryViewModel
import kotlinx.coroutines.launch
import retrofit2.Response

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(
    navController: NavController,
    viewModel: CategoryViewModel,
    token: String,
    isAdmin: Int,
    id: Int,
    name: String
) {

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val scope = rememberCoroutineScope()
    val gson = Gson()
    val context = LocalContext.current

    var quizzes: Response<QuizFilterResponse>? by remember {
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

    var isRefreshing by remember {
        mutableStateOf(false)
    }

    LaunchedEffect (toggle){
        error = HttpError("")
        showQuizzes = false
        retry = false
        try{
            quizzes = viewModel.filterQuizzes(token, id, page)
            isRefreshing = false
        }catch (e: Exception){
            retry = true
            error = HttpError("Please try again!")
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
    Scaffold (
        modifier = Modifier.fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MediumTopAppBar(
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    scrolledContainerColor = MaterialTheme.colorScheme.surface
                ),
                title = {
                    Text("Category: $name")
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.navigate(PagesScreens.HomeScreen.name){
                                popUpTo(PagesScreens.CategoryScreen.name+"/$id/$name"){
                                    inclusive = true
                                }
                                launchSingleTop = true
                            }
                        }
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ){ padding ->
    PullToRefreshBox(
        modifier = Modifier.padding(padding)
            .fillMaxSize(),
        isRefreshing = isRefreshing,
        onRefresh = {
            isRefreshing = true
            toggle = !toggle
        }
    ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
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
                                approved = it.approved,
                                onUserClick = {
                                    //todo add user click actions
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

}