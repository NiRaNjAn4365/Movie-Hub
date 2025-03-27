package com.example.moviehub.data.remote

import com.example.moviehub.models.MovieResponse
import com.example.moviehub.models.SearchResult
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import com.example.moviehub.BuildConfig

interface MovieApi {

    @Headers("Authorization: Bearer ${BuildConfig.MOVIE_API_KEY}")
    @GET("discover/movie")
    suspend fun getAllMovies(
        @Query("page") page: Int
    ): MovieResponse

    @Headers("Authorization: Bearer ${BuildConfig.MOVIE_API_KEY}")
    @GET("search/movie")
    suspend fun searchMovies(
        @Query("query") query: String,
        @Query("page") page: Int = 1,
        @Query("language") language: String = "en-US"
    ): SearchResult

    @Headers("Authorization: Bearer ${BuildConfig.MOVIE_API_KEY}")
    @GET("discover/movie")
    suspend fun getMovieByCateogery(
        @Query("with_genres") genreId: Int,
        @Query("page") page: Int=1    ):MovieResponse
}