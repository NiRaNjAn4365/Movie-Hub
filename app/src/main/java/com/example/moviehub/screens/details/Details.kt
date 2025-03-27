import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.moviehub.models.MovieResults
import com.google.gson.Gson
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

@Composable
fun Details(movieJson: String,navHostController: NavHostController) {
    val decodedMovieJson = URLDecoder.decode(movieJson, StandardCharsets.UTF_8.toString())
    val movie = Gson().fromJson(decodedMovieJson, MovieResults::class.java)
    Icon(
        imageVector = Icons.Default.ArrowBack,
        contentDescription = "Back  Arrow ",
        modifier = Modifier.padding(start = 10.dp,top=30.dp)
            .size(25.dp)
            .clickable {
                navHostController.popBackStack()
            }
    )
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(painter = rememberAsyncImagePainter("https://image.tmdb.org/t/p/w500${movie.poster_path}"),
             contentDescription = "Image")
        Text(text = movie.title, style = MaterialTheme.typography.headlineMedium)
        Text(text = "Rating: ${movie.vote_average}/10", style = MaterialTheme.typography.bodyMedium)
        Text(text = movie.overview, style = MaterialTheme.typography.bodySmall)
    }
}