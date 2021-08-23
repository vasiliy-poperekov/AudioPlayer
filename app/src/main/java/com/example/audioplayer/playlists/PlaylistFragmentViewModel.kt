package com.example.audioplayer.playlists

import androidx.lifecycle.*
import com.example.audioplayer.baseEntities.Playlist
import com.example.audioplayer.database.repositories.PlaylistRepository
import kotlinx.coroutines.Dispatchers
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