package com.example.audioplayer.songsOnPlaylist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.audioplayer.MainActivity
import com.example.audioplayer.baseEntities.Playlist
import com.example.audioplayer.baseEntities.Song
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.example.audioplayer.databinding.FragmentSongsOnPlaylistBinding
import com.example.audioplayer.playlists.PlaylistFragment.Companion.CLICKED_PLAYLIST_KEY
import com.example.audioplayer.songsOnPlaylist.dialog.DialogForChoosingSongs

class SongsOnPlaylistFragment(
    private val startPlayerFragment: (songsList: ArrayList<Song>, currentSong: Song) -> Unit
): Fragment() {

    var binding: FragmentSongsOnPlaylistBinding? = null
    val viewModel: SongsOnPlaylistFragmentViewModel by viewModel()
    lateinit var clickedPlaylist: Playlist
    lateinit var songsList: ArrayList<Song>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSongsOnPlaylistBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = SongsOnPlaylistFragmentAdapter({startPlayer(it)}, {viewModel.deleteSongFromPlaylist(it, clickedPlaylist)})
        binding?.rvSongsOnPlaylist?.adapter = adapter

        this.arguments = (activity as MainActivity).bundle
        clickedPlaylist = arguments?.getSerializable(CLICKED_PLAYLIST_KEY) as Playlist

        binding?.tvPlaylistName?.text = clickedPlaylist.name

        viewModel.pathSongsListOnPlaylistToLiveData(clickedPlaylist)
        viewModel.liveDataSongs.observe(viewLifecycleOwner, {
            adapter.submitList(it)
            songsList = it
        })

        binding?.fabAddNewSongToPlaylist?.setOnClickListener {
            DialogForChoosingSongs{createSongsWithPlaylist(it)}.show(parentFragmentManager, DIALOG_TAG)
        }

        binding?.bvToBack?.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    fun startPlayer(currentSong: Song){
        startPlayerFragment(songsList, currentSong)
    }

    fun createSongsWithPlaylist(songsList: MutableList<Song>){
        songsList.forEach {
            viewModel.addSongWithPlayList(Song(it.uri, it.title, it.subtitle, it.duration, clickedPlaylist.name), clickedPlaylist)
        }
    }

    companion object{
        const val DIALOG_TAG = "DIALOG_TAG"
    }
}