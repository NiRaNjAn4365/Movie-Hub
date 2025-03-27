package com.example.moviehub.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.moviehub.util.Constants.MOVIE_REMOTE_KEY_TABLE

@Entity(tableName = MOVIE_REMOTE_KEY_TABLE)
data class MovieRemoteKeyTable
    (
            @PrimaryKey(autoGenerate = false)
            val id:Int,
            val prevPage:Int?,
            val nextPage:Int?
            )
{
}
