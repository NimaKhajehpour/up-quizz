package com.nima.upquizz.navigation.main

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nima.upquizz.datastore.AppDatastore
import com.nima.upquizz.screens.LoginScreen
import com.nima.upquizz.screens.RegisterScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val datastore = AppDatastore(context)
    val token = datastore.getToken.collectAsState(null).value

    if (token != null){
        NavHost(
            navController,
            startDestination = if (token.isBlank()) MainScreens.Login.name else MainScreens.MainScreen.name
        ) {
            composable(MainScreens.Login.name) {
                LoginScreen(navController, koinViewModel())
            }
            composable(MainScreens.Register.name) {
                RegisterScreen(navController, koinViewModel())
            }
            composable(MainScreens.MainScreen.name) {
            }
        }
    }
}

