package com.example.audioplayer.database.repositories

import com.example.audioplayer.baseEntities.Playlist
import com.example.audioplayer.baseEntities.Song
import com.example.audioplayer.database.daos.SongDao
import com.example.audioplayer.database.entities.SongEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SongRepository(
    private val songDao: SongDao
) {
    fun getSongsListOnPlaylist(playlist: Playlist): List<Song> =
        songDao.getSongsListOnPlaylist(playlist.name).map {
            Song(it.uri, it.title, it.subtitle, it.duration, it.playlistName)
        }

    fun addSongWithPlayList(song: Song){
        songDao.addSongWithNewPlayList(SongEntity(song.uri, song.title, song.subtitle, song.duration, song.playlistName!!))
    }

    fun deleteSongFromPlaylist(song: Song){
        songDao.deleteSongFromPlaylist(SongEntity(song.uri, song.title, song.subtitle, song.duration, song.playlistName!!))
    }
}