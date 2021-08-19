package com.example.audioplayer.songsOnPlaylist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.audioplayer.baseEntities.Playlist
import com.example.audioplayer.baseEntities.Song
import com.example.audioplayer.database.repositories.SongRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SongsOnPlaylistFragmentViewModel(
    private val songRepository: SongRepository
): ViewModel() {

    val liveDataSongs: MutableLiveData<List<Song>> = MutableLiveData()

    fun getSongsListOnPlaylist(playlist: Playlist) {
        viewModelScope.launch(Dispatchers.IO) {
            liveDataSongs.postValue(songRepository.getSongsListOnPlaylist(playlist))
        }
    }

    fun addSongWithPlayList(song: Song, playlist: Playlist){
        viewModelScope.launch(Dispatchers.IO) {
            songRepository.addSongWithPlayList(song)
            getSongsListOnPlaylist(playlist)
        }
    }

    fun deleteSongFromPlaylist(song: Song, playlist: Playlist){
        viewModelScope.launch(Dispatchers.IO) {
            songRepository.deleteSongFromPlaylist(song)
            getSongsListOnPlaylist(playlist)
        }
    }
}