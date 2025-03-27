package com.example.moviehub.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.moviehub.models.MovieRemoteKeyTable

@Dao
interface MovieRemoteKeyDao {

        @Query("SELECT * FROM movie_remote_key_table WHERE id =:id")
        fun getRemoteKeys(id: Int): MovieRemoteKeyTable?

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun addAllRemoteKeys(remoteKeys: List<MovieRemoteKeyTable>)

        @Query("DELETE FROM movie_remote_key_table")
        suspend fun deleteAllRemoteKeys()
}