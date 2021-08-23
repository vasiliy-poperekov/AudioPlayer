package com.example.audioplayer.songsOnPlaylist.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.audioplayer.FileRepository
import com.example.audioplayer.baseEntities.Song
import com.example.audioplayer.databinding.DialogForChoosingSongsBinding

class DialogForChoosingSongs(
    val createSongsWithPlaylist: (MutableList<Song>) -> Unit
): DialogFragment() {

    var binding: DialogForChoosingSongsBinding? = null
    val choosedSongsList = mutableListOf<Song>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogForChoosingSongsBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = DialogAdapter({addSongToList(it)}, {deleteSongFromList(it)})
        binding?.rvDialogWithSongs?.adapter = adapter
        adapter.submitList(FileRepository.giveSongFiles(requireActivity()))

        binding?.addAllChoosedSongs?.setOnClickListener {
            createSongsWithPlaylist(choosedSongsList)
            dismiss()
        }
    }

    fun addSongToList(song: Song){
        choosedSongsList.add(song)
    }

    fun deleteSongFromList(song: Song){
        choosedSongsList.remove(song)
    }
}