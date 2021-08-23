package com.example.audioplayer

import android.app.Activity
import android.app.Application
import androidx.room.Room
import com.example.audioplayer.database.SongDatabase
import com.example.audioplayer.database.repositories.PlaylistRepository
import com.example.audioplayer.database.repositories.SongRepository
import com.example.audioplayer.playlists.PlaylistFragmentViewModel
import com.example.audioplayer.songsOnPlaylist.SongsOnPlaylistFragmentViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class PlayerApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@PlayerApplication)
            modules(listOf(viewModels, repositoryModels, databaseModels))
        }
    }

    private val viewModels = module {
        viewModel { PlaylistFragmentViewModel(get()) }
        viewModel { SongsOnPlaylistFragmentViewModel(get()) }
    }

    private val repositoryModels = module {
        factory { PlaylistRepository(get()) }
        factory { SongRepository(get()) }
    }

    private val databaseModels = module {
        single { Room.databaseBuilder(applicationContext, SongDatabase::class.java, "song_database").build() }
        factory { get<SongDatabase>().playListDao() }
        factory { get<SongDatabase>().songDao() }
    }
}