package com.example.audioplayer

import android.app.Notification
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.audioplayer.allSongList.SongsListFragment.Companion.SONGS_LIST_FRAGMENT_TAG
import com.example.audioplayer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var bundle: Bundle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        createPlayerFragment()

        Notification.MediaStyle()
    }

    private fun createPlayerFragment(){
        supportFragmentManager.beginTransaction()
            .add(binding.fragmentContainer.id, MainFragment(), SONGS_LIST_FRAGMENT_TAG)
            .addToBackStack(null)
            .commit()
    }

    fun reverseFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction()
            .replace(binding.fragmentContainer.id, fragment, SONGS_LIST_FRAGMENT_TAG)
            .addToBackStack(null)
            .commit()
    }
}