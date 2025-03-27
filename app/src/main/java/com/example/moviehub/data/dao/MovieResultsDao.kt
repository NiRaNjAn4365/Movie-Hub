package com.example.moviehub.data.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.moviehub.models.MovieResults

@Dao
interface MovieResultsDao {

    @Query("SELECT * FROM movie_table")
    fun getAllMovies(): PagingSource<Int, MovieResults>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMovies(movieResults: List<MovieResults>)

    @Query("DELETE FROM movie_table")
    suspend fun deleteAllMovies()
}