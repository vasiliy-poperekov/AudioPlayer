package com.example.audioplayer.database.repositories

import com.example.audioplayer.baseEntities.Playlist
import com.example.audioplayer.baseEntities.Song
import com.example.audioplayer.database.daos.SongDao
import com.example.audioplayer.database.entities.SongEntity

class SongRepository(
    private val songDao: SongDao
) {
    suspend fun getSongsListOnPlaylist(playlist: Playlist): ArrayList<Song> {
        val arraySongList = arrayListOf<Song>()
        songDao.getSongsListOnPlaylist(playlist.name).mapTo(arraySongList)
        {
            Song(it.uri, it.title, it.subtitle, it.duration, it.playlistName)
        }
        return arraySongList
    }


    fun addSongWithPlayList(song: Song){
        songDao.addSongWithNewPlayList(SongEntity(song.uri, song.title, song.subtitle, song.duration, song.playlistName!!))
    }

    fun deleteSongFromPlaylist(song: Song){
        songDao.deleteSongFromPlaylist(SongEntity(song.uri, song.title, song.subtitle, song.duration, song.playlistName!!))
    }
}