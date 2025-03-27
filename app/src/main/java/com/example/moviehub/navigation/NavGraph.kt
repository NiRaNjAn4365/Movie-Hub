package com.example.moviehub.navigation

import Details
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.paging.ExperimentalPagingApi
import coil.annotation.ExperimentalCoilApi
import com.example.moviehub.screens.database.authentication.AuthViewModel
import com.example.moviehub.screens.database.screens.LoginScreen
import com.example.moviehub.screens.database.screens.SignUpScreen
import com.example.moviehub.screens.database.screens.UserInformationScreen
import com.example.moviehub.screens.details.Categories
import com.example.moviehub.screens.details.Profile
import com.example.moviehub.screens.home.HomeScreen
import com.example.moviehub.screens.search.SearchScreen

@ExperimentalCoilApi
@ExperimentalPagingApi
@Composable
fun SetupNavGraph(navController: NavHostController, authViewModel: AuthViewModel) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(route = Screen.Home.route){
            HomeScreen(navController = navController)
        }
        composable(route = Screen.Search.route){
            SearchScreen(navController = navController)
        }
        composable(
            "details_screen/{movieJson}",
            arguments = listOf(navArgument("movieJson") { type = NavType.StringType })
        ) { backStackEntry ->
            val movieJson = backStackEntry.arguments?.getString("movieJson") ?: ""
            Details(movieJson = movieJson,navController)
        }
        composable(route=Screen.Cateogeires.route) {
            Categories(navController)
        }
        composable(route=Screen.Profie.route) {
            Profile(navController,authViewModel)
        }
        composable(route=Screen.Login.route) {
            LoginScreen(navController,authViewModel)
        }

        composable(route=Screen.SignUp.route) {
            SignUpScreen(navController,authViewModel)
        }
        composable(route=Screen.Information.route) {
            UserInformationScreen(navController)
        }
    }
}