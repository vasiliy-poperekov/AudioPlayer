package com.example.audioplayer.songsOnPlaylist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.audioplayer.MainActivity
import com.example.audioplayer.baseEntities.Playlist
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.example.audioplayer.databinding.FragmentSongsOnPlaylistBinding
import com.example.audioplayer.playlists.PlaylistFragment.Companion.CLICKED_PLAYLIST_KEY
import com.example.audioplayer.songsOnPlaylist.dialog.DialogAdapter
import com.example.audioplayer.songsOnPlaylist.dialog.DialogForChoosingSongs

class SongsOnPlaylistFragment: Fragment() {

    var binding: FragmentSongsOnPlaylistBinding? = null
    val viewModel: SongsOnPlaylistFragmentViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSongsOnPlaylistBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = SongsOnPlaylistFragmentAdapter()
        binding?.rvSongsOnPlaylist?.adapter = adapter

        this.arguments = (activity as MainActivity).bundle
        val clickedPlaylist = arguments?.getSerializable(CLICKED_PLAYLIST_KEY) as Playlist

        binding?.tvPlaylistName?.text = clickedPlaylist.name

        viewModel.getSongsListOnPlaylist(clickedPlaylist)
        viewModel.liveDataSongs.observe(viewLifecycleOwner, {
            adapter.submitList(it)
        })

        binding?.fabAddNewSongToPlaylist?.setOnClickListener {
            DialogForChoosingSongs().show(parentFragmentManager, "dialog")
        }
    }
}