package com.example.audioplayer.player.dialog

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.audioplayer.baseEntities.Song
import com.example.audioplayer.databinding.ItemSongForDialogBinding
import com.example.audioplayer.songsOnPlaylist.dialog.DialogDiffUtilItemCallBack

class DialogAdapter(
    private val playSong: (Song) -> Unit
): androidx.recyclerview.widget.ListAdapter<Song, DialogViewHolder>(DialogDiffUtilItemCallBack()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DialogViewHolder =
        DialogViewHolder(ItemSongForDialogBinding.inflate(LayoutInflater.from(parent.context), parent, false), {playSong(it)})

    override fun onBindViewHolder(holder: DialogViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}