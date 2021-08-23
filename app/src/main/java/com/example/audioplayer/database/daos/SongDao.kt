package com.example.audioplayer.database.daos

import androidx.room.*
import com.example.audioplayer.database.entities.SongEntity

@Dao
interface SongDao {

    @Query("SELECT * FROM songentity WHERE playlistName = :playlistName")
    suspend fun getSongsListOnPlaylist(playlistName: String): List<SongEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addSongWithNewPlayList(songEntity: SongEntity)

    @Delete
    fun deleteSongFromPlaylist(songEntity: SongEntity)
}