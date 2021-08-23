package com.example.audioplayer.songsOnPlaylist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.audioplayer.baseEntities.Song
import com.example.audioplayer.databinding.ItemSongForPlaylistBinding

class SongsOnPlaylistFragmentAdapter(
    private val startPlayer: (Song) -> Unit,
    private val deleteSong: (Song) -> Unit
): ListAdapter<Song, SongsOnPlaylistFragmentViewHolder>(SongsOnPlaylistFragmentDiffUtillItemCallBack()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SongsOnPlaylistFragmentViewHolder =
        SongsOnPlaylistFragmentViewHolder(ItemSongForPlaylistBinding.inflate(LayoutInflater.from(parent.context), parent, false), { startPlayer(it) }, {deleteSong(it)})

    override fun onBindViewHolder(holder: SongsOnPlaylistFragmentViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}