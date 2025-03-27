package com.example.moviehub.data.paging

import androidx.paging.*
import androidx.room.withTransaction
import com.example.moviehub.data.MovieDatabase
import com.example.moviehub.data.remote.MovieApi
import com.example.moviehub.models.MovieRemoteKeyTable
import com.example.moviehub.models.MovieResults
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalPagingApi::class)
class MovieRemoteMediator(
    private val movieApi: MovieApi,
    private val movieDatabase: MovieDatabase
) : RemoteMediator<Int, MovieResults>() {

    private val movieResultDao = movieDatabase.movieResultDao()
    private val movieRemoteKeyDao = movieDatabase.movieRemoteKeyDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MovieResults>
    ): MediatorResult {
        return try {
            val currentPage = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextPage?.minus(1) ?: 1
                }
                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    val prevPage = remoteKeys?.prevPage
                        ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                    prevPage
                }
                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    val nextPage = remoteKeys?.nextPage
                        ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                    nextPage
                }
            }

            val response = movieApi.getAllMovies(page = currentPage)
            val movies = response.results
            val totalPages = response.total_pages
            val endOfPaginationReached = currentPage >= totalPages || movies.isEmpty()

            withContext(Dispatchers.IO) {
                movieDatabase.withTransaction {
                    if (loadType == LoadType.REFRESH) {
                        movieResultDao.deleteAllMovies()
                        movieRemoteKeyDao.deleteAllRemoteKeys()
                    }
                    val keys = movies.map { movie ->
                        MovieRemoteKeyTable(
                            id = movie.id,
                            prevPage = if (currentPage == 1) null else currentPage - 1,
                            nextPage = if (endOfPaginationReached) null else currentPage + 1
                        )
                    }
                    movieRemoteKeyDao.addAllRemoteKeys(remoteKeys = keys)
                    movieResultDao.addMovies(movieResults = movies)
                }
            }

            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, MovieResults>
    ): MovieRemoteKeyTable? {
        return withContext(Dispatchers.IO) {
            state.anchorPosition?.let { position ->
                state.closestItemToPosition(position)?.id?.let { id ->
                    movieRemoteKeyDao.getRemoteKeys(id = id)
                }
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(
        state: PagingState<Int, MovieResults>
    ): MovieRemoteKeyTable? {
        return withContext(Dispatchers.IO) {
            state.pages
                .firstOrNull { it.data.isNotEmpty() }
                ?.data
                ?.firstOrNull()
                ?.let { movie -> movieRemoteKeyDao.getRemoteKeys(id = movie.id) }
        }
    }

    private suspend fun getRemoteKeyForLastItem(
        state: PagingState<Int, MovieResults>
    ): MovieRemoteKeyTable? {
        return withContext(Dispatchers.IO) {
            state.pages
                .lastOrNull { it.data.isNotEmpty() }
                ?.data
                ?.lastOrNull()
                ?.let { movie -> movieRemoteKeyDao.getRemoteKeys(id = movie.id) }
        }
    }
}