package com.example.moviehub.screens.home

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.ExperimentalPagingApi
import androidx.paging.compose.collectAsLazyPagingItems
import coil.annotation.ExperimentalCoilApi
import com.example.moviehub.navigation.Screen
import com.example.moviehub.ui.screens.ListContent

@ExperimentalPagingApi
@ExperimentalCoilApi
@Composable
fun HomeScreen(
    navController: NavHostController,
    homeViewModel: HomeViewModel = hiltViewModel(),

) {
    val getAllImages = homeViewModel.getAllMovies.collectAsLazyPagingItems()

    Scaffold(
        topBar = {
            HomeTopBar(
                onSearchClicked = {
                    navController.navigate(Screen.Search.route)
                }
            )
        },
        content = { paddingValues ->
            ListContent(
                items = getAllImages,
                contentPadding = paddingValues,
                onMovieClick = { movieJson ->
                    navController.navigate("details_screen/$movieJson")
                })

        }

    )

}
