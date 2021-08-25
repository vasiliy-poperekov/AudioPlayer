package com.example.audioplayer.allSongList

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.audioplayer.FileRepository
import com.example.audioplayer.MainActivity
import com.example.audioplayer.baseEntities.Song
import com.example.audioplayer.databinding.FragmentListOfSongsBinding

class SongsListFragment(
    private val startPlayerFragment: (songsList: ArrayList<Song>, currentSong: Song) -> Unit
) : Fragment() {

    var binding: FragmentListOfSongsBinding? = null
    var songsList: ArrayList<Song> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListOfSongsBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (ContextCompat.checkSelfPermission(
                activity?.applicationContext!!,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                activity?.applicationContext!!,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) && ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    Manifest.permission.RECORD_AUDIO
                )
            ) {
                ActivityCompat.requestPermissions(
                    requireActivity(), arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO
                    ), 1
                )
            } else {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO),
                    1
                )
            }
        } else {
            songsList = FileRepository.giveSongFiles(requireActivity())
        }

        val songsListAdapter = SongsListAdapter { pathSongToPlayer(it) }
        songsListAdapter.submitList(songsList)
        binding?.rvSongsList?.adapter = songsListAdapter
    }

    fun pathSongToPlayer(song: Song) {
        startPlayerFragment(songsList, song)
    }

    companion object {
        const val SONGS_LIST_FRAGMENT_TAG = "SONGS_LIST_FRAGMENT_TAG"
    }
}