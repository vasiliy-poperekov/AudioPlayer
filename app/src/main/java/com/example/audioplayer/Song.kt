package com.example.audioplayer

import android.net.Uri
import android.os.Parcelable
import java.io.Serializable

data class Song(
    val uri: String,
    val title: String,
    val subtitle: String
) : Serializable