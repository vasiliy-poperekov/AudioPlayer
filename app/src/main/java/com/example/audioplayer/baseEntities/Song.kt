package com.example.audioplayer.baseEntities

import android.os.Parcel
import android.os.Parcelable

data class Song(
    val uri: String,
    val title: String,
    val subtitle: String,
    val duration: Int,
    var playlistName: String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readString()
    ) {
    }

    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(uri)
        dest?.writeString(title)
        dest?.writeString(subtitle)
        dest?.writeInt(duration)
        dest?.writeString(playlistName)
    }

    companion object CREATOR : Parcelable.Creator<Song> {
        override fun createFromParcel(parcel: Parcel): Song {
            return Song(parcel)
        }

        override fun newArray(size: Int): Array<Song?> {
            return arrayOfNulls(size)
        }
    }

}