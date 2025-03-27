package com.example.moviehub.navigation

sealed class Screen(val route: String){
    object Home: Screen("home_screen")
    object Search: Screen("search_screen")
    object Detail:Screen("detail_screen")
    object Login:Screen("login_screen")
    object SignUp:Screen("signup_screen")
    object Profie:Screen("profile_screen")
    object Cateogeires:Screen("cateogeries_screen")
    object Information:Screen("information_screen")

}