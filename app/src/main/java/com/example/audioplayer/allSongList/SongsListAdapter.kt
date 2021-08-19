package com.example.audioplayer.allSongList

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.audioplayer.baseEntities.Song
import com.example.audioplayer.databinding.ItemSongBinding

class SongsListAdapter(
    private val clickSong: (Song, Int) -> Unit
) : androidx.recyclerview.widget.ListAdapter<Song, SongViewHolder>(SongDiffUtilItemCallBack()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        return SongViewHolder(ItemSongBinding.inflate(LayoutInflater.from(parent.context), parent, false), clickSong)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}