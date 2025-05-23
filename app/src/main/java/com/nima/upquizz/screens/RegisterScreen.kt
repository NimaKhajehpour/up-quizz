package com.nima.upquizz.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.gson.Gson
import com.nima.upquizz.components.AppTextFieldWithClear
import com.nima.upquizz.datastore.AppDatastore
import com.nima.upquizz.navigation.main.MainScreens
import com.nima.upquizz.network.models.errors.http.HttpError
import com.nima.upquizz.network.models.errors.validation.ValidationError
import com.nima.upquizz.network.models.requests.signup.UserCreateModel
import com.nima.upquizz.network.models.responses.token.TokenResponse
import com.nima.upquizz.viewmodels.RegisterViewModel
import kotlinx.coroutines.launch
import retrofit2.Response

@Composable
fun RegisterScreen(
    navController: NavController,
    viewModel: RegisterViewModel
) { 
    val scope = rememberCoroutineScope()
    val gson = Gson()
    val context = LocalContext.current
    val appDatastore = AppDatastore(context)

    var userRequest: UserCreateModel by remember {
        mutableStateOf(UserCreateModel("","",null, ""))
    }

    var overallError by remember {
        mutableStateOf(HttpError(detail = ""))
    }

    var registerResponse: Response<TokenResponse>? by remember {
        mutableStateOf(null)
    }

    var errorResponse by remember {
        mutableStateOf<ValidationError?>(null)
    }

    LaunchedEffect (registerResponse){
        if (registerResponse != null){
            when (registerResponse!!.code()) {
                200 -> {
                    appDatastore.saveToken(registerResponse!!.body()!!.access_token)
                    navController.navigate(MainScreens.MainScreen.name){
                        popUpTo(MainScreens.Register.name){
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
                400 -> {
                    overallError = gson.fromJson(registerResponse!!.errorBody()!!.string(), HttpError::class.java)
                }
                422 -> {
                    errorResponse = gson.fromJson(registerResponse!!.errorBody()!!.string(), ValidationError::class.java)
                }
                else -> {
                    overallError = overallError.copy(detail = "Error Occurred")
                }
            }
        }
    }

    Column (
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        OutlinedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp, start = 32.dp, end = 32.dp, bottom = 16.dp),
            colors = CardDefaults.outlinedCardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
        ) {
            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp, horizontal = 15.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text("Register",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier =
                    Modifier.padding(bottom = 32.dp)
                )
                AppTextFieldWithClear(
                    value = userRequest.username,
                    onValueChanged = {
                        userRequest = userRequest.copy(username = it)
                    },
                    singleLine = true,
                    label = "Username *",
                    isError = errorResponse != null && errorResponse!!.hasUsernameError(),
                    supportingText = if (errorResponse != null) errorResponse!!.usernameError() else null
                ) {
                    userRequest = userRequest.copy(username = "")
                }
                AppTextFieldWithClear(
                    value = userRequest.display_name,
                    onValueChanged = {
                        userRequest = userRequest.copy(display_name = it)
                    },
                    singleLine = true,
                    label = "Display Name *",
                    isError = errorResponse != null && errorResponse!!.hasDisplayNameError(),
                    supportingText = if (errorResponse != null) errorResponse!!.displayNameError() else null
                ) {
                    userRequest = userRequest.copy(display_name = "")
                }
                AppTextFieldWithClear(
                    value = userRequest.password,
                    onValueChanged = {
                        userRequest = userRequest.copy(password = it)
                    },
                    singleLine = true,
                    label = "Password *",
                    visualTransformation = PasswordVisualTransformation('*'),
                    isError = errorResponse != null && errorResponse!!.hasPasswordError(),
                    supportingText = if (errorResponse != null) errorResponse!!.passwordError() else null
                ) {
                    userRequest = userRequest.copy(password = "")
                }
                Button(
                    onClick = {
                        scope.launch {
                            overallError = overallError.copy(detail = "")
                            errorResponse = null
                            try {
                                registerResponse = viewModel.register(userRequest)
                            }catch (e: Exception){
                                overallError = overallError.copy(detail = "Please Try Again!")
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp, horizontal = 16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    enabled = userRequest.isValid()
                ) {
                    Text("Register")
                }
            }
        }
        AnimatedVisibility(overallError.detail.isNotBlank()) {
            Text(overallError.detail, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(vertical = 8.dp, horizontal = 32.dp))
        }
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ){
            Text("Already have an account?",
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(end = 16.dp)
            )
            TextButton(
                onClick = {
                    navController.navigate(MainScreens.Login.name){
                        popUpTo(MainScreens.Register.name){
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                colors = ButtonDefaults.textButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.tertiary
                )
            ) {
                Text("Login")
            }
        }
    }
}