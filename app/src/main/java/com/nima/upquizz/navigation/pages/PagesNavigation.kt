package com.nima.upquizz.navigation.pages

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.nima.upquizz.screens.CategoriesScreen
import com.nima.upquizz.screens.CategoryScreen
import com.nima.upquizz.screens.HomeScreen
import com.nima.upquizz.screens.RateQuizScreen
import com.nima.upquizz.screens.TakeQuizScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun PagesNavigation(navController: NavHostController, token: String, isAdmin: Int) {
    NavHost(navController, startDestination = PagesScreens.HomeScreen.name){
        composable(PagesScreens.HomeScreen.name){
            HomeScreen(navController, token, isAdmin, koinViewModel())
        }
        composable(PagesScreens.CategoriesScreen.name){
            CategoriesScreen(
                navController, koinViewModel(), token, isAdmin
            )
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
        composable(PagesScreens.RateQuizScreen.name+"/{id}/{title}/{corrects}/{total}",
            arguments = listOf(
                navArgument("id"){type = NavType.IntType},
                navArgument("title"){type = NavType.StringType},
                navArgument("corrects"){type = NavType.IntType},
                navArgument("total"){type = NavType.IntType}
            )
            ){
            RateQuizScreen(
                token,
                navController,
                koinViewModel(),
                it.arguments?.getInt("id")!!,
                it.arguments?.getString("title")!!,
                it.arguments?.getInt("corrects")!!,
                it.arguments?.getInt("total")!!
            )
        }
        composable(PagesScreens.CategoryScreen.name+"/{id}/{name}",
            arguments = listOf(
                navArgument("id"){type = NavType.IntType},
                navArgument("name"){type = NavType.StringType},
            )
            ){
            CategoryScreen(navController, koinViewModel(), token, isAdmin, it.arguments!!.getInt("id"), it.arguments!!.getString("name")!!)
        }
    }
}