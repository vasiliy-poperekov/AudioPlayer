package com.example.audioplayer.songsOnPlaylist

import androidx.recyclerview.widget.DiffUtil
import com.example.audioplayer.baseEntities.Song

class SongsOnPlaylistFragmentDiffUtillItemCallBack: DiffUtil.ItemCallback<Song>() {
    override fun areItemsTheSame(oldItem: Song, newItem: Song): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Song, newItem: Song): Boolean {
        return oldItem.title == newItem.title
    }
}