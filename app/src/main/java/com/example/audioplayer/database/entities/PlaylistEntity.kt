package com.example.audioplayer.database.entities

import androidx.room.Entity

@Entity(primaryKeys = ["name"])
data class PlaylistEntity(
    val name: String
)