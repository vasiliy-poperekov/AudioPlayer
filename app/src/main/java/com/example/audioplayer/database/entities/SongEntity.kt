package com.example.audioplayer.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    primaryKeys = ["title", "subtitle", "playlistName"],
    foreignKeys = [ForeignKey(
        entity = PlaylistEntity::class,
        parentColumns = arrayOf("name"),
        childColumns = arrayOf("playlistName"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class SongEntity(
    val uri: String,
    val title: String,
    val subtitle: String,
    val duration: Int,
    val playlistName: String
)