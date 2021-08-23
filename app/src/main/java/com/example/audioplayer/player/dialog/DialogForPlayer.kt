package com.example.audioplayer.player.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.audioplayer.baseEntities.Song
import com.example.audioplayer.databinding.DialogForChoosingSongsBinding

class DialogForPlayer(
    private val songsList: ArrayList<Song>,
    private val playSong: (Song) -> Unit
): DialogFragment() {

    var binding: DialogForChoosingSongsBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogForChoosingSongsBinding.inflate(inflater, container, false)
        binding!!.tvDialogTitle.text = "Choose song"
        binding!!.addAllChoosedSongs.visibility = View.GONE
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = DialogAdapter{pathSong(it)}
        adapter.submitList(songsList)
        binding!!.rvDialogWithSongs.adapter = adapter
    }

    fun pathSong(song: Song){
        playSong(song)
        dismiss()
    }
}