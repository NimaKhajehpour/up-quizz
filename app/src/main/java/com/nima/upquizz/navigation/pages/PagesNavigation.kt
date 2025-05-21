package com.nima.upquizz.navigation.pages

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun PagesNavigation(navController: NavHostController) {
    NavHost(navController, startDestination = PagesScreens.HomeScreen.name){
        composable(PagesScreens.HomeScreen.name){
            Text("Home")
        }
        composable(PagesScreens.CategoriesScreen.name){
            Text("Cat")
        }
        composable(PagesScreens.UsersScreen.name){
            Text("User")
        }
        composable(PagesScreens.SettingsScreen.name){
            Text("Setting")
        }
    }
}