package com.example.audioplayer.playlists

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ListAdapter
import com.example.audioplayer.baseEntities.Playlist
import com.example.audioplayer.databinding.ItemPlaylistBinding

class PlaylistAdapter(
    private val deleteItem: (Playlist) -> Unit,
    private val clickPlaylist: (Playlist) -> Unit
): androidx.recyclerview.widget.ListAdapter<Playlist, PlaylistViewHolder>(PlaylistDiffUtilItemCallBack()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder =
        PlaylistViewHolder(ItemPlaylistBinding.inflate(LayoutInflater.from(parent.context), parent, false), deleteItem, clickPlaylist)

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}