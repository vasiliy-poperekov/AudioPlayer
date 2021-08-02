package com.example.audioplayer

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder

class PlayerService : Service(){

    lateinit var mediaPlayer: MediaPlayer

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        mediaPlayer = MediaPlayer.create(applicationContext, R.raw.music)
        mediaPlayer.start()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        mediaPlayer = MediaPlayer.create(applicationContext, R.raw.music)
//        mediaPlayer.start()
        val pi: PendingIntent? = intent?.getParcelableExtra(PlayerFragment.PEND_KEY)
        val sendedIntent = Intent().putExtra(PlayerFragment.RESULT, "hello")
        pi?.send(PlayerFragment.PEND_REC_CODE)
        return START_STICKY
    }

    fun playOrStopMusic() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
        } else {
            mediaPlayer.start()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}