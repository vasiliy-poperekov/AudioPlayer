package com.example.audioplayer.baseEntities

import android.net.Uri
import android.os.Parcelable
import java.io.Serializable

data class Song(
    val uri: String,
    val title: String,
    val subtitle: String,
    val duration: Int,
    var playlistName: String? = null
) : Serializable