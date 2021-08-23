package com.example.audioplayer.allSongList

import androidx.recyclerview.widget.RecyclerView
import com.example.audioplayer.baseEntities.Song
import com.example.audioplayer.databinding.ItemSongBinding

class SongViewHolder(
    private val binding: ItemSongBinding,
    private val clickSong: (Song) -> Unit
): RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Song){
        binding.tvItemSongTitle.text = item.title
        binding.tvItemSongSubtitle.text = item.subtitle
        itemView.setOnClickListener {
            clickSong(item)
        }
    }
}