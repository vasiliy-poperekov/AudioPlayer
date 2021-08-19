package com.example.audioplayer.playlists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.example.audioplayer.MainActivity
import com.example.audioplayer.allSongList.SongsListFragment
import com.example.audioplayer.baseEntities.Playlist
import com.example.audioplayer.databinding.FragmentPlaylistsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment(
    val clickOnPlaylist: () -> Unit
): Fragment() {

    var binding: FragmentPlaylistsBinding? = null
    private val viewModel: PlaylistFragmentViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val playlistAdapter = PlaylistAdapter({deleteLongClickedPlaylist(it)}, {openSongsOnPlaylist(it)})
        binding!!.rvPlaylist.adapter = playlistAdapter

        viewModel.liveDataPlaylists.observe((this as LifecycleOwner), {
            playlistAdapter.submitList(it.toMutableList())
        })

        binding!!.fabAddNewPlaylist.setOnClickListener {
            if (binding!!.etNewPlaylistName.text.toString() != ""){
                viewModel.addPlayList(Playlist(binding!!.etNewPlaylistName.text.toString()))
            } else {
                Toast.makeText(view.context, "Enter playlist's name", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun deleteLongClickedPlaylist(playlist: Playlist){
        viewModel.deletePlaylist(playlist)
    }

    fun openSongsOnPlaylist(playlist: Playlist){
        val bundle = Bundle()
        bundle.putSerializable(CLICKED_PLAYLIST_KEY, playlist)
        (activity as MainActivity).bundle = bundle
        clickOnPlaylist()
    }

    companion object{
        const val CLICKED_PLAYLIST_KEY = "CLICKED_PLAYLIST_KEY"
    }
}