package com.example.audioplayer.songsOnPlaylist

import androidx.recyclerview.widget.RecyclerView
import com.example.audioplayer.baseEntities.Song
import com.example.audioplayer.databinding.ItemSongForPlaylistBinding

class SongsOnPlaylistFragmentViewHolder(
    private val binding: ItemSongForPlaylistBinding,
    private val startPlayer: (Song) -> Unit,
    private val deleteSong: (Song) -> Unit
): RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Song){
        binding.tvItemSongTitle.text = item.title
        binding.tvItemSongSubtitle.text = item.subtitle
        itemView.setOnClickListener {
            startPlayer(item)
        }
        binding.ivDeleteSong.setOnClickListener {
            deleteSong(item)
        }
    }
}