package com.example.audioplayer.database

import androidx.room.TypeConverter
import com.example.audioplayer.database.entities.PlaylistEntity

class PlaylistsConverter {

    @TypeConverter
    fun fromPlaylistList(playlistsList: List<PlaylistEntity>): String {
        val playlistsToString = ""
        playlistsList.forEach {
            playlistsToString.plus("${it.name}, ")
        }
        return playlistsToString
    }

    @TypeConverter
    fun toPlaylistList(playlistAsString: String): List<PlaylistEntity>{
        val listWithStrings = playlistAsString.split(", ")
        val listOfPlaylists = mutableListOf<PlaylistEntity>()
        listWithStrings.forEach {
            listOfPlaylists.add(PlaylistEntity(it))
        }
        return listOfPlaylists.toList()
    }

}