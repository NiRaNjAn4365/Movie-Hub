package com.example.moviehub.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.moviehub.util.Constants.MOVIE_TABLE
import kotlinx.serialization.Serializable


@Serializable

@Entity(tableName = MOVIE_TABLE)

data class MovieResults
    (
         @PrimaryKey(autoGenerate = false)
         val id :Int,
         val title:String,
         val overview:String,
         val poster_path:String?,
         val vote_average:Double
            )
{
}

