package com.example.audioplayer.database.daos

import androidx.room.*
import com.example.audioplayer.database.entities.SongEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SongDao {

    @Query("SELECT * FROM songentity WHERE playlistName = :playlistName")
    fun getSongsListOnPlaylist(playlistName: String): List<SongEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addSongWithNewPlayList(songEntity: SongEntity)

    @Delete
    fun deleteSongFromPlaylist(songEntity: SongEntity)
}