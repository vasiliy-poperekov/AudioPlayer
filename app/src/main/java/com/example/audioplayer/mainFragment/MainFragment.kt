package com.example.audioplayer.mainFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.audioplayer.MainActivity
import com.example.audioplayer.allSongList.SongsListFragment
import com.example.audioplayer.baseEntities.Song
import com.example.audioplayer.databinding.FragmentMainBinding
import com.example.audioplayer.playlists.PlaylistFragment
import com.example.audioplayer.songsOnPlaylist.SongsOnPlaylistFragment
import com.google.android.material.tabs.TabLayoutMediator

class MainFragment : Fragment() {

    var binding: FragmentMainBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding!!.viewPager.adapter = MainFragPagesAdapter(
            this, mutableListOf(
                SongsListFragment { list: ArrayList<Song>, song:Song -> (activity as MainActivity).startPlayerFragment(list, song) },
                PlaylistFragment {
                    (activity as MainActivity).reverseFragment(SongsOnPlaylistFragment {
                            list: ArrayList<Song>, song:Song -> (activity as MainActivity).startPlayerFragment(list, song)
                    })
                }
            )
        )
        TabLayoutMediator(binding!!.tabLayout, binding!!.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "All songs"
                1 -> tab.text = "Playlists"
            }
        }.attach()
    }
}