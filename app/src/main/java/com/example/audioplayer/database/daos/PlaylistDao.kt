package com.example.audioplayer.database.daos

import androidx.room.*
import com.example.audioplayer.database.entities.PlaylistEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {

    @Query("SELECT * FROM playlistentity")
    fun getAllPlaylists(): Flow<List<PlaylistEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addPlaylist(playlistEntity: PlaylistEntity)

    @Delete
    fun deletePlaylist(playlistEntity: PlaylistEntity)

}