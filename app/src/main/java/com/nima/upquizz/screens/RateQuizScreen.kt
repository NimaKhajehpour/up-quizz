package com.nima.upquizz.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.SliderState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.gson.Gson
import com.nima.upquizz.navigation.pages.PagesScreens
import com.nima.upquizz.network.models.errors.http.HttpError
import com.nima.upquizz.viewmodels.RateQuizViewModel
import retrofit2.Response

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RateQuizScreen(
    token: String,
    navController: NavController,
    viewModel: RateQuizViewModel,
    id: Int,
    title: String,
    correctAnswers: Int,
    totalAnswers: Int
) {

    var quizProgress by remember {
        mutableFloatStateOf(0f)
    }

    LaunchedEffect(Unit) {
        quizProgress = correctAnswers.toFloat() / totalAnswers.toFloat()
    }

    val animatedProgress by animateFloatAsState(
        quizProgress, label = "progress",
        animationSpec = tween(
            durationMillis = 1000,
            easing = LinearOutSlowInEasing
        )
    )

    var rateQuiz by remember {
        mutableStateOf(false)
    }

    var quizRate by remember {
        mutableFloatStateOf(0f)
    }

    var rating by remember {
        mutableStateOf(false)
    }

    var ratingResponse: Response<Any>? by remember {
        mutableStateOf(null)
    }

    val gson = Gson()

    var error by remember {
        mutableStateOf(HttpError(""))
    }

    LaunchedEffect(rating) {
        if (rating) {
            try {
                ratingResponse = viewModel.rateQuiz(token, id, quizRate.toInt())
            } catch (e: Exception) {
                rating = false
                error = HttpError("Rating Failed, Please try again")
            }
        }
    }

    LaunchedEffect(ratingResponse) {
        if (ratingResponse != null) {
            when (ratingResponse!!.code()) {
                204 -> {
                    navController.navigate(PagesScreens.HomeScreen.name) {
                        popUpTo(PagesScreens.RateQuizScreen.name + "/$id/$title/$correctAnswers/$totalAnswers") {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }

                else -> {
                    rating = false
                    error = gson.fromJson(
                        ratingResponse!!.errorBody()!!.string(),
                        HttpError::class.java
                    )
                }
            }
        }
    }

    if (rating){
        BasicAlertDialog(
            onDismissRequest = {

            },
        ) {
            Column (
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                CircularProgressIndicator()
                Text("Saving your rating!")
            }
        }
    }

    if (error.detail.isNotBlank()){
        AlertDialog(
            onDismissRequest = {

            },
            confirmButton = {
                TextButton(
                    onClick = {
                        error = HttpError("")
                    }
                ) {
                    Text("Ok")
                }
            },
            title = {
                Text("An error occurred!")
            },
            text = {
                Text(error.detail)
            }
        )
    }

    BackHandler(true) {
        navController.navigate(PagesScreens.HomeScreen.name) {
            popUpTo(PagesScreens.RateQuizScreen.name + "/$id/$title/$correctAnswers/$totalAnswers") {
                inclusive = true
            }
            launchSingleTop = true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(
                onClick = {
                    if (!rateQuiz) {
                        navController.navigate(PagesScreens.HomeScreen.name) {
                            popUpTo(PagesScreens.RateQuizScreen.name + "/$id/$title/$correctAnswers/$totalAnswers") {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    } else {
                        rating = true
                    }
                },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.tertiary
                )
            ) {
                Text("Done", style = MaterialTheme.typography.labelLarge)
            }
        }

        Text(
            title,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        CircularProgressIndicator(
            progress = {
                animatedProgress
            },
            color = MaterialTheme.colorScheme.secondary,
            trackColor = MaterialTheme.colorScheme.secondaryContainer,
            modifier = Modifier
                .padding(bottom = 32.dp)
                .size(128.dp),
            strokeWidth = 15.dp,
            gapSize = 10.dp
        )
        Text(
            "Your Score:\n${(animatedProgress * 100).toInt()}%",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Checkbox(
                checked = rateQuiz,
                onCheckedChange = {
                    rateQuiz = it
                },
                modifier = Modifier.padding(end = 5.dp),
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.tertiary,
                    checkmarkColor = MaterialTheme.colorScheme.onTertiary,
                    uncheckedColor = MaterialTheme.colorScheme.tertiary
                )
            )
            Text("Rate the quiz?", style = MaterialTheme.typography.bodyMedium)
        }
        Slider(
            value = quizRate,
            onValueChange = {
                quizRate = it
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 32.dp),
            enabled = rateQuiz,
            valueRange = 1f..5f,
            steps = 3,
            colors = SliderDefaults.colors(
                activeTrackColor = MaterialTheme.colorScheme.primary,
                inactiveTrackColor = MaterialTheme.colorScheme.secondary,
                thumbColor = MaterialTheme.colorScheme.primary,
                activeTickColor = MaterialTheme.colorScheme.onPrimary,
                inactiveTickColor = MaterialTheme.colorScheme.onSecondary
            )
        )
    }
}