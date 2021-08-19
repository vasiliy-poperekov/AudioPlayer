package com.example.audioplayer.songsOnPlaylist.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.audioplayer.FileRepository
import com.example.audioplayer.R
import com.example.audioplayer.databinding.DialogForChoosingSongsBinding

class DialogForChoosingSongs: DialogFragment() {

    var binding: DialogForChoosingSongsBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.setTitle("Title")
        binding = DialogForChoosingSongsBinding.inflate(inflater, container, false)
        return binding?.root
    }



//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        val adb = AlertDialog.Builder(activity)
//            .setTitle("Title")
//            .setMessage("Message")
//            .setView(R.layout.dialog_for_choosing_songs)
//            .create()
//        return adb
//    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//
//        val adapter = DialogAdapter()
//        adapter.submitList(FileRepository.songList)
//        binding?.rvDialogWithSongs?.adapter = adapter
//
//    }
}