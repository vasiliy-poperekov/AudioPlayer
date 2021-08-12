package com.example.audioplayer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.audioplayer.SongsListFragment.Companion.SONGS_LIST_FRAGMENT_TAG
import com.example.audioplayer.databinding.ActivityMainBinding
import com.example.audioplayer.databinding.FragmentPlayerBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var bundle: Bundle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        createPlayerFragment()
    }

    private fun createPlayerFragment(){
        supportFragmentManager.beginTransaction()
            .add(binding.fragmentContainer.id, SongsListFragment { reverseFragment() }, SONGS_LIST_FRAGMENT_TAG)
            .addToBackStack(null)
            .commit()
    }

    fun reverseFragment(){
        supportFragmentManager.beginTransaction()
            .replace(binding.fragmentContainer.id, PlayerFragment(), SONGS_LIST_FRAGMENT_TAG)
            .addToBackStack(null)
            .commit()
    }
}