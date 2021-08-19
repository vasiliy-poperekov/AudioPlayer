package com.example.audioplayer.playlists

import androidx.recyclerview.widget.RecyclerView
import com.example.audioplayer.baseEntities.Playlist
import com.example.audioplayer.databinding.ItemPlaylistBinding

class PlaylistViewHolder(
    private val binding: ItemPlaylistBinding,
    private val deleteItem: (Playlist) -> Unit,
    private val clickPlaylist: (Playlist) -> Unit
): RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Playlist){
        binding.tvPlaylistTitle.text = item.name
        itemView.setOnLongClickListener {
            deleteItem(item)
            true
        }
        itemView.setOnClickListener {
            clickPlaylist(item)
        }
    }
}