package com.example.audioplayer

import android.app.Notification
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.audioplayer.allSongList.SongsListFragment.Companion.SONGS_LIST_FRAGMENT_TAG
import com.example.audioplayer.baseEntities.Song
import com.example.audioplayer.databinding.ActivityMainBinding
import com.example.audioplayer.mainFragment.MainFragment
import com.example.audioplayer.player.PlayerFragment
import com.example.audioplayer.player.PlayerFragment.Companion.PLAYER_FRAGMENT_TAG
import com.example.audioplayer.player.PlayerService.Companion.CURRENT_POSITION_FROM_NOTIFY
import com.example.audioplayer.player.PlayerService.Companion.PLAYED_LIST
import com.example.audioplayer.player.PlayerService.Companion.SONG_FROM_NOTIFY
import com.example.audioplayer.player.PlayerService.Companion.START_PLAYER_FRAGMENT_ACTION

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var bundle: Bundle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.action == START_PLAYER_FRAGMENT_ACTION) {
            val currentSong = intent.getParcelableExtra<Song>(SONG_FROM_NOTIFY)!!
            val playedList = intent.getParcelableArrayListExtra<Song>(PLAYED_LIST)!!
            val currentPosition = intent.getIntExtra(CURRENT_POSITION_FROM_NOTIFY, 0)
            supportFragmentManager.beginTransaction()
                .add(
                    binding.fragmentContainer.id,
                    PlayerFragment(playedList, currentSong, currentPosition),
                    PLAYER_FRAGMENT_TAG
                )
                .commit()
        } else {
            startMainFragment()
        }
    }

    private fun startMainFragment() {
        supportFragmentManager.beginTransaction()
            .add(binding.fragmentContainer.id, MainFragment(), SONGS_LIST_FRAGMENT_TAG)
            .commit()
    }

    fun reverseFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .add(binding.fragmentContainer.id, fragment, SONGS_LIST_FRAGMENT_TAG)
            .addToBackStack(null)
            .commit()
    }

    fun startPlayerFragment(songsList: ArrayList<Song>, currentSong: Song) {
        supportFragmentManager.beginTransaction()
            .add(
                binding.fragmentContainer.id,
                PlayerFragment(songsList, currentSong, 0),
                PLAYER_FRAGMENT_TAG
            )
            .addToBackStack(null)
            .commit()
    }
}