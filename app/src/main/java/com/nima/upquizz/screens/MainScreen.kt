package com.nima.upquizz.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.gson.Gson
import com.nima.upquizz.components.BottomNav
import com.nima.upquizz.datastore.AppDatastore
import com.nima.upquizz.navigation.main.MainScreens
import com.nima.upquizz.navigation.pages.PagesNavigation
import com.nima.upquizz.navigation.pages.PagesScreens
import com.nima.upquizz.network.models.errors.http.HttpError
import com.nima.upquizz.network.models.responses.profile.ProfileResponse
import com.nima.upquizz.viewmodels.MainViewModel
import retrofit2.Response

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavController,
    viewModel: MainViewModel
) {

    val gson = Gson()
    val pagesNavController = rememberNavController()
    val context = LocalContext.current
    val appDatastore = AppDatastore(context)
    val isAdmin = appDatastore.getAdmin.collectAsState(-1).value
    val token = appDatastore.getToken.collectAsState(null).value

    var userProfile: Response<ProfileResponse>? by remember {
        mutableStateOf(null)
    }

    var overallError by remember {
        mutableStateOf(HttpError(detail = ""))
    }

    var retry by remember {
        mutableStateOf(false)
    }

    var trigger by remember {
        mutableStateOf(false)
    }

    LaunchedEffect (token, isAdmin, trigger){
        if (!token.isNullOrBlank() && isAdmin==-1){
            try {
                userProfile = viewModel.getProfile(token)
            }catch (e: Exception){
                retry = true
                overallError = overallError.copy(detail = "Please Try Again")
            }
        }
    }

    LaunchedEffect (userProfile){
        if (userProfile != null){
            when (userProfile!!.code()){
                200 -> {
                    val admin = userProfile!!.body()!!.role
                    appDatastore.saveAdmin(if (admin == "user") 0 else 1)
                }
                404 -> {
                    retry = false
                    overallError = gson.fromJson(userProfile!!.errorBody()!!.string(), HttpError::class.java)
                }
                403 -> {
                    retry = false
                    overallError = gson.fromJson(userProfile!!.errorBody()!!.string(), HttpError::class.java)
                }
            }
        }
    }

    var destination by remember {
        mutableStateOf(PagesScreens.HomeScreen.name)
    }

    val mainPages = listOf(PagesScreens.UsersScreen.name, PagesScreens.HomeScreen.name, PagesScreens.SettingsScreen.name, PagesScreens.CategoriesScreen.name)

    LaunchedEffect(pagesNavController) {
        pagesNavController.currentBackStackEntryFlow.collect {
            destination = it.destination.route!!
        }
    }

    if (isAdmin == -1 && overallError.detail.isBlank()){
        BasicAlertDialog (
            onDismissRequest = {

            }
        ){
            Column (
                modifier = Modifier.fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                CircularProgressIndicator(

                )
                Text("Loading...")
            }
        }
    }
    if (isAdmin == -1 && overallError.detail.isNotBlank()){
        BasicAlertDialog (
            onDismissRequest = {

            }
        ){
            Column (
                modifier = Modifier.fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(overallError.detail)
                if (retry){
                    Button(
                        onClick = {
                            trigger = !trigger
                            retry = false
                            overallError = overallError.copy(detail = "")
                        }
                    ) {
                        Text("Retry")
                    }
                }else{
                    Button(
                        onClick = {
                            navController.navigate(MainScreens.Login.name){
                                popUpTo(MainScreens.MainScreen.name){
                                    inclusive = true
                                }
                                launchSingleTop = true
                            }
                        }
                    ) {
                        Text("Go Back")
                    }
                }
            }
        }
    }

    if (isAdmin != -1 && overallError.detail.isBlank() && !token.isNullOrBlank()){
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                BottomNav(pagesNavController, destination, mainPages)
            }
        ) { padding ->
            Column(
                modifier = Modifier.padding(padding)
            ) {
                PagesNavigation(pagesNavController, token, isAdmin)
            }
        }
    }
}