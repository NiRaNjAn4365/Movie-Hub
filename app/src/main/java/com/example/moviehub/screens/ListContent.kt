package com.example.moviehub.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import coil.compose.rememberAsyncImagePainter
import com.example.moviehub.models.MovieResults

import com.google.gson.Gson
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@ExperimentalPagingApi
@Composable
fun ListContent(
    items: LazyPagingItems<MovieResults>,
    contentPadding: PaddingValues,
    onMovieClick: (String) -> Unit
) {
    val loadState = items.loadState

    when {
        loadState.refresh is LoadState.Loading -> {
            CircularProgressIndicator(modifier = Modifier.fillMaxSize())
            return
        }
        loadState.refresh is LoadState.Error -> {
            val error = (loadState.refresh as LoadState.Error).error
            Text(text = "Error: ${error.localizedMessage}", color = Color.Red, modifier = Modifier.fillMaxSize())
            return
        }
        items.itemCount == 0 -> {
            Text(text = "No movies found.", modifier = Modifier.fillMaxSize(), textAlign = TextAlign.Center)
            return
        }
    }
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(top=10.dp),
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(items.itemCount) { index ->
            val movie = items[index]
            if (movie != null) {
                MovieItem(movie) {
                    val movieJson = Gson().toJson(movie)
                    val encodedMovie = URLEncoder.encode(movieJson, StandardCharsets.UTF_8.toString())
                    onMovieClick(encodedMovie)
                }
            }
        }
    }
}

@Composable
fun MovieItem(movie: MovieResults, onClick: (MovieResults) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .clickable {
                onClick(movie)
            },
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = if (movie.poster_path != null) {
                        "https://image.tmdb.org/t/p/w500${movie.poster_path}"
                    } else {
                        "https://via.placeholder.com/500x750?text=No+Image+Available"
                    },
                    error = rememberAsyncImagePainter("https://via.placeholder.com/500x750?text=Error+Loading+Image"),
                    placeholder = rememberAsyncImagePainter("https://via.placeholder.com/500x750?text=Loading...")
                ),
                contentDescription = "Movie Poster",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.7f)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.3f)
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    modifier = Modifier.padding(top = 10.dp),
                    text = "Rating  ${movie.vote_average}/10",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewMovieItem() {
    val sampleMovie = MovieResults(
        id = 1,
        title = "Inception",
        overview = "A mind-bending thriller about dream invasion.",
        poster_path = "/9gk7adHYeDvHkCSEqAvQNLV5Uge.jpg",
        vote_average = 8.8
    )


}