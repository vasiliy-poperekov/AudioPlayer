package com.example.audioplayer.player.dialog

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.audioplayer.baseEntities.Song
import com.example.audioplayer.databinding.ItemSongForDialogBinding

class DialogViewHolder(
    val binding: ItemSongForDialogBinding,
    val playSong: (Song) -> Unit
): RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Song){
        binding.tvItemSongTitle.text = item.title
        binding.tvItemSongSubtitle.text = item.subtitle
        binding.cbAddSongInPlaylist.visibility = View.GONE
        itemView.setOnClickListener {
            playSong(item)
        }
    }
}