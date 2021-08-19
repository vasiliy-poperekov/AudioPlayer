package com.example.audioplayer

import android.app.*
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
import com.example.audioplayer.PlayerFragment.Companion.BROADCAST_ACTION_FOR_TV
import com.example.audioplayer.PlayerFragment.Companion.CURRENT_SONG_INFO
import com.example.audioplayer.PlayerFragment.Companion.PLAY_NEXT_FROM_NOTIFY
import com.example.audioplayer.PlayerFragment.Companion.PLAY_OR_STOP_FROM_NOTIFY
import com.example.audioplayer.PlayerFragment.Companion.PLAY_PREVIOUS_FROM_NOTIFY
import com.example.audioplayer.PlayerFragment.Companion.PRESS_NEXT_BUTTON_ACTION
import com.example.audioplayer.PlayerFragment.Companion.PRESS_PREVIOUS_BUTTON_ACTION
import com.example.audioplayer.baseEntities.Song
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerService: Service(){

    lateinit var mediaPlayer: MediaPlayer
    lateinit var sendedIntent: Intent
    var position: Int = 0
    val songsList = FileRepository.songList
    lateinit var notifyManager: NotificationManager
    lateinit var notifBuilder: Notification

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

        initManager()

        when(intent?.action){
            PLAY_OR_STOP_FROM_NOTIFY -> {
                playOrPause()
                buildAndShowNotify()
            }
            PLAY_NEXT_FROM_NOTIFY -> {
                if (position<songsList.size-1){
                    position++
                    playSong(songsList[position])
                    sendInfoForTV()
                    buildAndShowNotify()
                }
            }
            PLAY_PREVIOUS_FROM_NOTIFY -> {
                if (position>0){
                    position--
                    playSong(songsList[position])
                    sendInfoForTV()
                    buildAndShowNotify()
                }
            }
            PRESS_NEXT_BUTTON_ACTION -> {
                if (position<songsList.size-1){
                    position++
                    playSong(songsList[position])
                    sendInfoForTV()
                    buildAndShowNotify()
                }
            }
            PRESS_PREVIOUS_BUTTON_ACTION -> {
                if (position>0){
                    position--
                    playSong(songsList[position])
                    sendInfoForTV()
                    buildAndShowNotify()
                }
            }
        }

        if (intent?.hasExtra(PlayerFragment.CURRENT_SONG)!!){
            val sendedSong = intent?.getSerializableExtra(PlayerFragment.CURRENT_SONG) as Song
            playSong(sendedSong)
            buildAndShowNotify()
        }

        if (intent?.hasExtra(PlayerFragment.SEEKBAR_ACTION)!!){
            mediaPlayer.seekTo(intent?.getIntExtra(PlayerFragment.SEEKBAR_ACTION, 0))
        }

        sendTimeForSB()

        return START_NOT_STICKY
    }

    fun playOrPause() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
        } else {
            mediaPlayer.start()
        }
    }

    fun playSong(song: Song){
        mediaPlayer.stop()
        mediaPlayer = MediaPlayer.create(applicationContext, song.uri.toUri())
        mediaPlayer.setOnCompletionListener {
            if (position<songsList.size-1){
                position++
                playSong(songsList[position])
                sendInfoForTV()
                sendTimeForSB()
            } else {
                position = 0
                playSong(songsList[position])
                sendInfoForTV()
                sendTimeForSB()
            }
        }
        mediaPlayer.start()
    }

    fun sendInfoForTV(){
        sendedIntent = Intent(BROADCAST_ACTION_FOR_TV)
            .putExtra(CURRENT_SONG_INFO, songsList[position])
        sendBroadcast(sendedIntent)
    }

    fun sendTimeForSB(){
        sendedIntent = Intent(PlayerFragment.BROADCAST_ACTION_FOR_SB)
        CoroutineScope(Dispatchers.Main).launch {
            while (mediaPlayer!=null){
                sendedIntent.putExtra(PlayerFragment.RECEIVED_TIME, mediaPlayer.currentPosition)
                sendBroadcast(sendedIntent)
                delay(1000)
            }
        }
    }

    fun initManager(){
        notifyManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (notifyManager.getNotificationChannel(PlayerFragment.NOTIFICATION_CHANNEL_ID) == null) {
                val channel = NotificationChannel(
                    PlayerFragment.NOTIFICATION_CHANNEL_ID,
                    PlayerFragment.NOTIF_CHAR_SEQUENCE,
                    NotificationManager.IMPORTANCE_DEFAULT
                )
                notifyManager.createNotificationChannel(channel)
            }
        }
    }

    fun buildAndShowNotify(){
        notifBuilder =
            NotificationCompat.Builder(applicationContext,
                PlayerFragment.NOTIFICATION_CHANNEL_ID
            ).apply {
                setSmallIcon(R.drawable.ic_baseline_music_note_24)
                setContentTitle(songsList[position].title)
                setContentText(songsList[position].subtitle)
                addAction(
                    R.drawable.ic_baseline_fast_rewind_24, "Back", PendingIntent
                        .getService(
                            applicationContext,
                            0,
                            Intent(applicationContext, PlayerService::class.java)
                                .setAction(PLAY_PREVIOUS_FROM_NOTIFY),
                            0
                        )
                )
                if (mediaPlayer.isPlaying){
                    addAction(
                        R.drawable.ic_baseline_pause_24, "Pause", PendingIntent
                            .getService(
                                applicationContext,
                                0,
                                Intent(applicationContext, PlayerService::class.java)
                                    .setAction(PLAY_OR_STOP_FROM_NOTIFY),
                                0
                            )
                    )
                } else {
                    addAction(
                        R.drawable.ic_baseline_play_arrow_24, "Play", PendingIntent
                            .getService(
                                applicationContext,
                                0,
                                Intent(applicationContext, PlayerService::class.java)
                                    .setAction(PLAY_OR_STOP_FROM_NOTIFY),
                                0
                            )
                    )
                }
                addAction(
                    R.drawable.ic_baseline_fast_forward_24, "Next", PendingIntent
                        .getService(
                            applicationContext,
                            0,
                            Intent(applicationContext, PlayerService::class.java)
                                .setAction(PLAY_NEXT_FROM_NOTIFY),
                            0
                        )
                )
                setStyle(androidx.media.app.NotificationCompat.MediaStyle())
                setSilent(true)
            }.build()
        notifyManager.notify(PlayerFragment.NOTIFICATION_ID, notifBuilder)
    }
}