package com.nima.upquizz.navigation.pages

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.nima.upquizz.screens.HomeScreen
import com.nima.upquizz.screens.TakeQuizScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun PagesNavigation(navController: NavHostController, token: String, isAdmin: Int) {
    NavHost(navController, startDestination = PagesScreens.HomeScreen.name){
        composable(PagesScreens.HomeScreen.name){
            HomeScreen(navController, token, isAdmin, koinViewModel())
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
        composable(PagesScreens.TakeQuizScreen.name+"/{id}",
            arguments = listOf(navArgument("id"){type = NavType.IntType})
        ){
            TakeQuizScreen(navController, token, isAdmin, it.arguments?.getInt("id"), koinViewModel())
        }
        composable(PagesScreens.RateQuizScreen.name+"/{id}/{corrects}/{total}",
            arguments = listOf(
                navArgument("id"){type = NavType.IntType},
                navArgument("corrects"){type = NavType.IntType},
                navArgument("total"){type = NavType.IntType}
            )
            ){
            Text("RateScreen")
        }
    }
}