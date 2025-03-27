package com.example.moviehub.screens.search

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.ExperimentalPagingApi
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.moviehub.ui.screens.ListContent

@OptIn(ExperimentalPagingApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SearchScreen(
    navController: NavHostController,
    searchViewModel: SearchViewModel = hiltViewModel()
) {
    val searchQuery by searchViewModel.searchQuery.collectAsState()
    val searchedMovies = searchViewModel.searchedMovies.collectAsLazyPagingItems()

    Scaffold(
        topBar = {
            SearchWidget(
                text = searchQuery,
                onTextChange = { searchViewModel.updateSearchQuery(it) },
                onSearchClicked = {},
                onCloseClicked = { navController.popBackStack() }
            )
        },
        content = { paddingValues ->
            ListContent(
                items = searchedMovies,
                contentPadding = paddingValues,
                onMovieClick = { movieJson ->
                    navController.navigate("details_screen/$movieJson")
                }
            )
        }
    )
}