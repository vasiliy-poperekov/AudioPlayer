package com.example.audioplayer

import android.content.ContentUris
import android.provider.MediaStore
import androidx.fragment.app.FragmentActivity
import com.example.audioplayer.baseEntities.Song

class FileRepository {
    companion object{
        fun giveSongFiles(activity: FragmentActivity): ArrayList<Song>{
            val songList = arrayListOf<Song>()
            val songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            val songCursor = activity.contentResolver.query(songUri, null, null, null, null)

            if (songCursor!=null&&songCursor.moveToNext()){
                val songID = songCursor.getColumnIndex(MediaStore.Audio.Media._ID)
                val songTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE)
                val songSubtitle = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)
                val songDuration = songCursor.getColumnIndex(MediaStore.Audio.Media.DURATION)

                do {
                    val currentID = songCursor.getLong(songID)
                    val currentTitle = songCursor.getString(songTitle)
                    val currentSubtitle = songCursor.getString(songSubtitle)
                    val currentDuration = songCursor.getInt(songDuration)
                    val currentSongUri = ContentUris.appendId(songUri.buildUpon(), currentID).build()
                    songList.add(Song(currentSongUri.toString(), currentTitle, currentSubtitle, currentDuration))
                } while (songCursor.moveToNext())
            }
            return songList
        }
    }
}