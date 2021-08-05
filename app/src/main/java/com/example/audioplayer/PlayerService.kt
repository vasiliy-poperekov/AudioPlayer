package com.example.audioplayer

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerService: Service(){

    lateinit var mediaPlayer: MediaPlayer
    lateinit var sendedIntent: Intent

    private val binder = MyBinder()
    override fun onBind(intent: Intent?): IBinder? = binder

    override fun onCreate() {
        mediaPlayer = MediaPlayer.create(applicationContext, R.raw.music)
        mediaPlayer.start()
    }

    inner class MyBinder: Binder(){
        fun getService(): PlayerService = this@PlayerService
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        if (intent?.action == PlayerFragment.PLAY_OR_STOP_ACTION_FROM_NOTIFY){
            playOrStopMusic()
        }

        if (intent?.hasExtra(PlayerFragment.SEEKBAR_ACTION)!!){
            mediaPlayer.seekTo(intent?.getIntExtra(PlayerFragment.SEEKBAR_ACTION, 0))
        }

        sendedIntent = Intent(PlayerFragment.BROADCAST_ACTION_FOR_SB)
        sendTimeForSB()

        return START_NOT_STICKY
    }

    fun playOrStopMusic() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
        } else {
            mediaPlayer.start()
        }
    }

    fun sendTimeForSB(){
        CoroutineScope(Dispatchers.Main).launch {
            while (mediaPlayer!=null){
                sendedIntent.putExtra(PlayerFragment.RECEIVED_TIME, mediaPlayer.currentPosition)
                sendBroadcast(sendedIntent)
                delay(1000)
            }
        }
    }
}