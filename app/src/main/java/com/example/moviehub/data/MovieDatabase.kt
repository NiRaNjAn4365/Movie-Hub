package com.example.moviehub.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.moviehub.data.dao.MovieRemoteKeyDao
import com.example.moviehub.data.dao.MovieResultsDao
import com.example.moviehub.models.MovieRemoteKeyTable
import com.example.moviehub.models.MovieResults


@Database(entities = [MovieResults::class,MovieRemoteKeyTable::class], version = 1,exportSchema = false)
abstract class MovieDatabase:RoomDatabase() {
       abstract  fun movieResultDao():MovieResultsDao
       abstract fun movieRemoteKeyDao():MovieRemoteKeyDao

}