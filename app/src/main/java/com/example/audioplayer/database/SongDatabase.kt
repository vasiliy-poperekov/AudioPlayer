package com.example.audioplayer.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.audioplayer.database.daos.PlaylistDao
import com.example.audioplayer.database.daos.SongDao
import com.example.audioplayer.database.entities.PlaylistEntity
import com.example.audioplayer.database.entities.SongEntity

@Database(entities = [SongEntity::class, PlaylistEntity::class], version = 1)
abstract class SongDatabase: RoomDatabase() {
    abstract fun songDao(): SongDao
    abstract fun playListDao(): PlaylistDao
}