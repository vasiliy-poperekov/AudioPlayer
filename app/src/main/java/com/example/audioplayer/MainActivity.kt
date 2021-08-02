package com.example.audioplayer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.audioplayer.databinding.ActivityMainBinding
import com.example.audioplayer.databinding.FragmentPlayerBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        createPlayerFragment()
    }

    private fun createPlayerFragment(){
        supportFragmentManager.beginTransaction()
            .add(binding.fragmentContainer.id, PlayerFragment(), PlayerFragment.PLAYER_FRAGMENT_TAG)
            .addToBackStack(null)
            .commit()
    }
}