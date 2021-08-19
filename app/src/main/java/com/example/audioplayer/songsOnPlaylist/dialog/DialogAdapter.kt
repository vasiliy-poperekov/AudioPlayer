package com.example.audioplayer.songsOnPlaylist.dialog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.audioplayer.allSongList.SongDiffUtilItemCallBack
import com.example.audioplayer.baseEntities.Song
import com.example.audioplayer.databinding.ItemSongForDialogBinding

class DialogAdapter: ListAdapter<Song, DialogViewHolder>(SongDiffUtilItemCallBack()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DialogViewHolder =
        DialogViewHolder(ItemSongForDialogBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: DialogViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}