package com.example.audioplayer

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.audioplayer.databinding.FragmentListOfSongsBinding

class SongsListFragment(
    private val reverseFragment: () -> Unit
): Fragment() {

    var binding: FragmentListOfSongsBinding? = null
    var songsList = mutableListOf<Song>()

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

        if (ContextCompat.checkSelfPermission(activity?.applicationContext!!, Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)){
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
            } else {
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
            }
        } else {
            songsList = FileRepository.giveSongFiles(requireActivity())
        }

        val songsListAdapter = SongsListAdapter{song: Song, i: Int -> pathSongToPlayer(song, i) }
        songsListAdapter.submitList(songsList)
        binding?.rvSongsList?.adapter = songsListAdapter
    }

    fun pathSongToPlayer(song: Song, position: Int){
        val bundle = Bundle()
        bundle.putSerializable(CLICKED_SONG_KEY, song)
        bundle.putInt(CLICKED_SONG_POSITION_KEY, position)
        (activity as MainActivity).bundle = bundle
        reverseFragment()
    }

    companion object{
        const val CLICKED_SONG_KEY = "CLICKED_SONG_KEY"
        const val CLICKED_SONG_POSITION_KEY = "CLICKED_SONG_POSITION_KEY"
        const val SONGS_LIST_FRAGMENT_TAG = "SONGS_LIST_FRAGMENT_TAG"
    }
}