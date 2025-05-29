package com.nima.upquizz.screens

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.gson.Gson
import com.nima.upquizz.components.QuestionTextCard
import com.nima.upquizz.navigation.pages.PagesNavigation
import com.nima.upquizz.navigation.pages.PagesScreens
import com.nima.upquizz.network.models.errors.http.HttpError
import com.nima.upquizz.network.models.requests.TakenQuizRequest
import com.nima.upquizz.network.models.responses.quiz.item.QuizResponse
import com.nima.upquizz.viewmodels.TakeQuizViewModel
import retrofit2.Response

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TakeQuizScreen(
    navController: NavController,
    token: String,
    isAdmin: Int,
    id: Int?,
    viewModel: TakeQuizViewModel
) {

    val gson = Gson()

    var error by remember {
        mutableStateOf(HttpError(""))
    }
    var retry by remember {
        mutableStateOf(false)
    }
    var toggle by remember {
        mutableStateOf(false)
    }
    var showQuiz by remember {
        mutableStateOf(false)
    }

    var saving by remember {
        mutableStateOf(false)
    }

    var quiz: Response<QuizResponse>? by remember {
        mutableStateOf(null)
    }

    var savedError: HttpError by remember {
        mutableStateOf(HttpError(""))
    }

    var savedResponse: Response<Any>? by remember {
        mutableStateOf(null)
    }

    var retrySaving by remember {
        mutableStateOf(false)
    }

    BackHandler (true){
        navController.navigate(PagesScreens.HomeScreen.name){
            popUpTo(PagesScreens.TakeQuizScreen.name+"/${id!!}"){
                inclusive = true
            }
            launchSingleTop = true
        }
    }

    LaunchedEffect (toggle){
        if (id != null && id!= 0){
            retry = false
            error = HttpError("")
            try {
                quiz = viewModel.getQuizById(token, id)
            }catch (e: Exception){
                retry = true
                error = HttpError("Please try again!")
            }
        }
    }

    LaunchedEffect (quiz){
        if (quiz != null){
            when(quiz!!.code()){
                200 -> {
                    if (quiz!!.body()!!.approved){
                        showQuiz = true
                        retry = false
                        error = HttpError("")
                    }else{
                        retry = false
                        error = HttpError("Cant take an unapproved quiz!")
                    }
                }
                else -> {
                    retry = false
                    error = gson.fromJson(quiz!!.errorBody()!!.string(), HttpError::class.java)
                }
            }
        }
    }

    if (!showQuiz && error.detail.isBlank()){
        Column (
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            CircularProgressIndicator()
        }
    }

    if (error.detail.isNotBlank()){
        Column (
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(error.detail)
            if (retry){
                Button(
                    onClick = {
                        toggle = !toggle
                    }
                ) {
                    Text("Retry")
                }
            }
        }
    }

    if(saving){
        BasicAlertDialog(
            onDismissRequest = {

            }
        ) {
            Column (
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                CircularProgressIndicator()
                Text("Saving your data")
            }
        }
    }

    if (showQuiz && quiz!!.body()!!.questions.isNotEmpty()){
        val questions by remember(quiz) {
            mutableStateOf(quiz!!.body()!!.questions)
        }
        var questionIndex by remember {
            mutableIntStateOf(0)
        }
        val question by remember (questionIndex){
            mutableStateOf(questions[questionIndex])
        }
        var correctAnswers by remember {
            mutableIntStateOf(0)
        }
        val correctAnswerIndex by remember(question) {
            mutableIntStateOf(question.answers.indexOf(question.answers.first { it.isCorrect }))
        }

        var selectedAnswer by remember {
            mutableIntStateOf(-1)
        }

        LaunchedEffect (saving){
            if (saving){
                savedError = HttpError("")
                val takenQuiz = TakenQuizRequest(correct_answers = correctAnswers, quiz_id = id!!, total_answers = questions.size)
                try{
                    savedResponse = viewModel.addTakenQuiz(token, takenQuiz)
                }catch (e: Exception){
                    savedError = HttpError("An Error Occurred, your progress is not saved!\nWant to try again?")
                    saving = false
                    retrySaving = true
                }
            }
        }

        LaunchedEffect (savedResponse){
            if (savedResponse != null){
                when(savedResponse!!.code()){
                    204 ->{
                        navController.navigate(PagesScreens.RateQuizScreen.name+"/$id/$correctAnswers/${questions.size}"){
                            popUpTo(PagesScreens.TakeQuizScreen.name+"/$id"){
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    }else ->{
                        saving = false
                        savedError = gson.fromJson(savedResponse!!.errorBody()!!.string(), HttpError::class.java)
                    }
                }
            }
        }

        if (savedError.detail.isNotBlank()){
            if (retrySaving){
                AlertDialog(
                    onDismissRequest = {

                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                saving = true
                                retrySaving = false
                            }
                        ) {
                            Text("Retry")
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = {
                                navController.navigate(PagesScreens.HomeScreen.name){
                                    popUpTo(PagesScreens.TakeQuizScreen.name+"/${id!!}"){
                                        inclusive = true
                                    }
                                    launchSingleTop = true
                                }
                            }
                        ) {
                            Text("Go Back")
                        }
                    },
                    title = {
                        Text("An Error Occurred!")
                    },
                    text = {
                        Text(savedError.detail)
                    }
                )
            }else{
                AlertDialog(
                    onDismissRequest = {

                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                navController.navigate(PagesScreens.HomeScreen.name){
                                    popUpTo(PagesScreens.TakeQuizScreen.name+"/${id!!}"){
                                        inclusive = true
                                    }
                                    launchSingleTop = true
                                }
                            }
                        ) {
                            Text("Go Back")
                        }
                    },
                    title = {
                        Text("An Error Occurred!")
                    },
                    text = {
                        Text(savedError.detail)
                    }
                )
            }

        }

        Column (
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp, vertical = 8.dp),
                progress = {
                    (questionIndex+1f/questions.size )
                },
                color = MaterialTheme.colorScheme.tertiary,
                trackColor = MaterialTheme.colorScheme.tertiaryContainer
            )
            QuestionTextCard(text = question.text)
            question.answers.forEachIndexed { index, item ->
                OutlinedButton(
                    onClick = {
                        if (selectedAnswer == -1){
                            selectedAnswer = index
                            if (index == correctAnswerIndex) {
                                correctAnswers++
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp, vertical = 4.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = if (selectedAnswer == -1){
                            MaterialTheme.colorScheme.tertiary
                        } else{
                            if (correctAnswerIndex == index){
                                Color.Green
                            }else{
                                Color.Red
                            }
                        }
                    ),
                    border = BorderStroke(1.dp, color = if (selectedAnswer == -1){
                        MaterialTheme.colorScheme.tertiaryContainer
                    } else{
                        if (correctAnswerIndex == index){
                            Color.Green
                        }else{
                            Color.Red
                        }
                    })
                ) {
                    Text(item.text)
                }
            }
            Button(
                onClick = {
                    selectedAnswer = -1
                    if (questionIndex < questions.size-1){
                        questionIndex++
                    }else{
                        saving = true
                    }
                },
                modifier = Modifier.fillMaxWidth()
                    .padding(vertical = 4.dp, horizontal = 32.dp),
                shape = RoundedCornerShape(15.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                enabled = selectedAnswer != -1
            ) {
                Text(if (questionIndex < questions.size-1) "Next" else "Finish")
            }
        }

    }
    if (showQuiz && quiz!!.body()!!.questions.isEmpty()){
        Column (
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text("This Quiz has no questions :(")
        }
    }

}