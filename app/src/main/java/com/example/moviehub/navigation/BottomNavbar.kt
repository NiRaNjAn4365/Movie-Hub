package com.example.moviehub.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed  class BottomNavbar(val routes:String,val text:String,val icon: ImageVector) {
    object Home:BottomNavbar("home_screen","Home",Icons.Default.Home)
    object Cateogeries:BottomNavbar("cateogeries_screen","Cateogeries",Icons.Default.Build)
    object Profile:BottomNavbar("profile_screen","Profile",Icons.Default.Person)
}