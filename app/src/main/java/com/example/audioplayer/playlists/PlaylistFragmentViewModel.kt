package com.example.audioplayer.playlists

import androidx.lifecycle.*
import com.example.audioplayer.baseEntities.Playlist
import com.example.audioplayer.baseEntities.Song
import com.example.audioplayer.database.entities.SongEntity
import com.example.audioplayer.database.repositories.PlaylistRepository
import com.example.audioplayer.database.repositories.SongRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch

class PlaylistFragmentViewModel(
    private val playlistRepository: PlaylistRepository
): ViewModel() {

    val liveDataPlaylists: LiveData<List<Playlist>> = playlistRepository.getAllPlaylists().asLiveData()

    fun addPlayList(playlist: Playlist){
        viewModelScope.launch(Dispatchers.IO) {
            playlistRepository.addPlaylist(playlist)
        }
    }

    fun deletePlaylist(playlist: Playlist){
        viewModelScope.launch(Dispatchers.IO) {
            playlistRepository.deletePlaylist(playlist)
        }
    }
}