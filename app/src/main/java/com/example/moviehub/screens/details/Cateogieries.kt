package com.example.moviehub.screens.details

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.rememberAsyncImagePainter
import com.example.moviehub.models.MovieResults
import com.google.gson.Gson
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun Categories(navHostController: NavHostController) {
    val viewModel: CategoriesViewModel = hiltViewModel()
    val categories = listOf(
        "Action" to 28,
        "Comedy" to 35,
        "Drama" to 18,
        "Horror" to 27,
        "Romance" to 10749,
        "Sci-Fi" to 878,
        "Thriller" to 53
    )

    var selectedGenreId by remember { mutableStateOf(28) }
    LaunchedEffect(Unit) {
        viewModel.setGenreId(28)
    }
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            categories.forEach { (name, id) ->
                Button(onClick = {
                    selectedGenreId = id
                    viewModel.setGenreId(id)
                }) {
                    Text(text = name)
                }
            }
        }

        val movies = viewModel.getCategoryMovies.collectAsLazyPagingItems()
        MovieGridScreen(movies, onMovieClick = { movieJson ->
            navHostController.navigate("details_screen/$movieJson")
        })
    }
}

@Composable
fun MovieGridScreen(movies: LazyPagingItems<MovieResults>, onMovieClick: (String) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier.padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(movies.itemCount) { index ->
            val movie = movies[index]
            if (movie != null) {
                MovieItem(movie){
                    val movieJson = Gson().toJson(movie)
                    val encodedMovie = URLEncoder.encode(movieJson, StandardCharsets.UTF_8.toString())
                    onMovieClick(encodedMovie)
                }
            }
        }
    }
}

@Composable
fun MovieItem(movie: MovieResults,onClick: (MovieResults) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.7f)
            .clip(RoundedCornerShape(12.dp))
            .clickable {
                onClick(movie)
            },
        shape = RoundedCornerShape(12.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = rememberAsyncImagePainter("https://image.tmdb.org/t/p/w500${movie.poster_path}"),
                contentDescription = "Movie Poster",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(12.dp))
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = movie.title,
                style = MaterialTheme.typography.titleSmall,
                maxLines = 1
            )
            Text(
                text = "Rating: ${movie.vote_average}/10",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}