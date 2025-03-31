package com.example.moviehub.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.moviehub.data.remote.MovieApi
import com.example.moviehub.models.MovieResults

class CategoeryPagingSource(
    private val movieApi: MovieApi,
    private val genreId:Int
): PagingSource<Int, MovieResults>() {


   override fun getRefreshKey(state: PagingState<Int, MovieResults>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }


    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieResults> {
        val currentPage = params.key ?: 1
        return try{
            val response = movieApi.getMovieByCateogery(genreId, currentPage)
            val movies = response.results ?: emptyList()
            val endOfPaginationReached = response.results.isEmpty()
            LoadResult.Page(
                data = movies,
                prevKey = if (currentPage == 1) null else currentPage - 1,
                nextKey = if (endOfPaginationReached) null else currentPage + 1
            )
        }catch(e:Exception){
            LoadResult.Error(e)
        }    }


}

