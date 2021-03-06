package com.example.audioplayer.songsOnPlaylist.dialog

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.audioplayer.baseEntities.Song
import com.example.audioplayer.databinding.ItemSongForDialogBinding

class DialogAdapter(
    val addSong: (Song) -> Unit,
    val deleteSong: (Song) -> Unit
): androidx.recyclerview.widget.ListAdapter<Song, DialogViewHolder>(DialogDiffUtilItemCallBack()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DialogViewHolder =
        DialogViewHolder(ItemSongForDialogBinding.inflate(LayoutInflater.from(parent.context), parent, false), addSong, deleteSong)

    override fun onBindViewHolder(holder: DialogViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}