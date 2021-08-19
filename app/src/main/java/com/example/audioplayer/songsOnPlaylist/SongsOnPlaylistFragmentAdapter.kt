package com.example.audioplayer.songsOnPlaylist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.audioplayer.baseEntities.Song
import com.example.audioplayer.databinding.ItemSongBinding

class SongsOnPlaylistFragmentAdapter: ListAdapter<Song, SongsOnPlaylistFragmentViewHolder>(SongsOnPlaylistFragmentDiffUtillItemCallBack()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SongsOnPlaylistFragmentViewHolder =
        SongsOnPlaylistFragmentViewHolder(ItemSongBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: SongsOnPlaylistFragmentViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}