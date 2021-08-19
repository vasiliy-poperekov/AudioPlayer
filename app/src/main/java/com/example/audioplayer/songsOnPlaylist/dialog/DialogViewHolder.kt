package com.example.audioplayer.songsOnPlaylist.dialog

import androidx.recyclerview.widget.RecyclerView
import com.example.audioplayer.baseEntities.Song
import com.example.audioplayer.databinding.ItemSongForDialogBinding

class DialogViewHolder(
    val binding: ItemSongForDialogBinding
): RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Song){
        binding.tvItemSongTitle.text = item.title
        binding.tvItemSongSubtitle.text = item.subtitle
    }
}