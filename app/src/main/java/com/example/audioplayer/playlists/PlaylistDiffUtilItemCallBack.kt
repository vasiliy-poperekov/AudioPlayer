package com.example.audioplayer.playlists

import androidx.recyclerview.widget.DiffUtil
import com.example.audioplayer.baseEntities.Playlist

class PlaylistDiffUtilItemCallBack: DiffUtil.ItemCallback<Playlist>() {
    override fun areItemsTheSame(oldItem: Playlist, newItem: Playlist): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Playlist, newItem: Playlist): Boolean {
        return oldItem.name == newItem.name
    }
}