package com.example.audioplayer.songsOnPlaylist

import androidx.recyclerview.widget.RecyclerView
import com.example.audioplayer.baseEntities.Song
import com.example.audioplayer.databinding.ItemSongBinding

class SongsOnPlaylistFragmentViewHolder(
    private val binding: ItemSongBinding
): RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Song){
        binding.tvItemSongTitle.text = item.title
        binding.tvItemSongSubtitle.text = item.subtitle
    }
}