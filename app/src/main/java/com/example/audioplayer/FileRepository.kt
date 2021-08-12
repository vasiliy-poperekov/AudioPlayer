package com.example.audioplayer

import android.content.ContentUris
import android.provider.MediaStore
import androidx.core.net.toUri
import androidx.fragment.app.FragmentActivity

class FileRepository {
    companion object{
        val songList = mutableListOf<Song>()
        fun giveSongFiles(activity: FragmentActivity): MutableList<Song>{
            val songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            val songCursor = activity.contentResolver.query(songUri, null, null, null, null)

            if (songCursor!=null&&songCursor.moveToNext()){
                val songID = songCursor.getColumnIndex(MediaStore.Audio.Media._ID)
                val songTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE)
                val songSubtitle = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)

                do {
                    val currentID = songCursor.getLong(songID)
                    val currentTitle = songCursor.getString(songTitle)
                    val currentSubtitle = songCursor.getString(songSubtitle)
                    val currentSongUri = ContentUris.appendId(songUri.buildUpon(), currentID).build()
                    songList.add(Song(currentSongUri.toString(), currentTitle, currentSubtitle))
                } while (songCursor.moveToNext())
            }

            return songList
        }
    }
}