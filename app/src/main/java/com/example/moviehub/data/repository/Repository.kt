package com.example.moviehub.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.moviehub.data.MovieDatabase
import com.example.moviehub.data.paging.CategoeryPagingSource
import com.example.moviehub.data.paging.MovieRemoteMediator
import com.example.moviehub.data.paging.SearchPagingSource
import com.example.moviehub.data.remote.MovieApi
import com.example.moviehub.models.MovieResults
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class Repository @Inject constructor(
    private val movieApi: MovieApi,
    private val movieDatabase: MovieDatabase
) {

    @OptIn(ExperimentalPagingApi::class)
    fun getAllMovies(): Flow<PagingData<MovieResults>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            remoteMediator = MovieRemoteMediator(movieApi, movieDatabase),
            pagingSourceFactory = { movieDatabase.movieResultDao().getAllMovies() }
        ).flow
    }

    fun searchMovies(query: String): Flow<PagingData<MovieResults>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                prefetchDistance = 5,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                SearchPagingSource(movieApi = movieApi, query = query,  "en-US")
            }
        ).flow
    }

    fun getMoviesByCateogeries(genreId:Int):Flow<PagingData<MovieResults>>{
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                prefetchDistance = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                CategoeryPagingSource(movieApi=movieApi,genreId=genreId)
            }
        ).flow
    }
}