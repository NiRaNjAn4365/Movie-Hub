package com.example.moviehub.screens

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.moviehub.navigation.BottomNavbar

@Composable
fun BottomBar(navHostController: NavHostController){
    val screens= listOf(
        BottomNavbar.Home,
        BottomNavbar.Cateogeries,
        BottomNavbar.Profile
    )
    NavigationBar {
    screens.forEach{
        screens->
        NavigationBarItem(
            selected = false,
            label = { Text(text = screens.text) },
            icon = { Icon(imageVector = screens.icon, contentDescription = "navbar_icon") },
            onClick = {navHostController.navigate(screens.routes)}
        )
    }
    }
}