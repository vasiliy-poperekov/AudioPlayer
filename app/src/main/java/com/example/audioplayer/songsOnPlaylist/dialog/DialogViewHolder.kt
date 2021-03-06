package com.example.audioplayer.songsOnPlaylist.dialog

import androidx.recyclerview.widget.RecyclerView
import com.example.audioplayer.baseEntities.Song
import com.example.audioplayer.databinding.ItemSongForDialogBinding

class DialogViewHolder(
    val binding: ItemSongForDialogBinding,
    val addSong: (Song) -> Unit,
    val deleteSong: (Song) -> Unit
): RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Song){
        binding.tvItemSongTitle.text = item.title
        binding.tvItemSongSubtitle.text = item.subtitle

        binding.cbAddSongInPlaylist.setOnClickListener {
            if (binding.cbAddSongInPlaylist.isChecked){
                addSong(item)
            } else {
                deleteSong(item)
            }
        }
    }
}