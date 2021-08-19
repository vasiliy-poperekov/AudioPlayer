package com.example.audioplayer.database.repositories

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.audioplayer.baseEntities.Playlist
import com.example.audioplayer.database.daos.PlaylistDao
import com.example.audioplayer.database.entities.PlaylistEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistRepository(
    private val playlistDao: PlaylistDao
) {
    fun getAllPlaylists(): Flow<List<Playlist>> =
        playlistDao.getAllPlaylists().map {
            it.map {
                Playlist(it.name)
            }
        }

    fun addPlaylist(playlist: Playlist){
        playlistDao.addPlaylist(PlaylistEntity(playlist.name))
    }

    fun deletePlaylist(playlist: Playlist){
        playlistDao.deletePlaylist(PlaylistEntity(playlist.name))
    }
}