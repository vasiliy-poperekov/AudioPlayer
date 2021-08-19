package com.example.audioplayer.allSongList

import androidx.recyclerview.widget.DiffUtil
import com.example.audioplayer.baseEntities.Song

class SongDiffUtilItemCallBack: DiffUtil.ItemCallback<Song>(){
    override fun areItemsTheSame(oldItem: Song, newItem: Song): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Song, newItem: Song): Boolean {
        return oldItem.title == newItem.title
    }
}