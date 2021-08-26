package com.example.audioplayer.player

import android.app.*
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.*
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
import com.example.audioplayer.MainActivity
import com.example.audioplayer.R
import com.example.audioplayer.baseEntities.Song
import com.example.audioplayer.player.PlayerFragment.Companion.BROADCAST_ACTION_FOR_SB
import com.example.audioplayer.player.PlayerFragment.Companion.BROADCAST_ACTION_FOR_TV
import com.example.audioplayer.player.PlayerFragment.Companion.CURRENT_SONG
import com.example.audioplayer.player.PlayerFragment.Companion.CURRENT_SONG_INFO
import com.example.audioplayer.player.PlayerFragment.Companion.GIVED_CURRENT_POSITION
import com.example.audioplayer.player.PlayerFragment.Companion.LIST_SONGS
import com.example.audioplayer.player.PlayerFragment.Companion.PLAY_NEXT_FROM_NOTIFY
import com.example.audioplayer.player.PlayerFragment.Companion.PLAY_OR_STOP_FROM_NOTIFY
import com.example.audioplayer.player.PlayerFragment.Companion.PLAY_PREVIOUS_FROM_NOTIFY
import com.example.audioplayer.player.PlayerFragment.Companion.PRESS_NEXT_BUTTON_ACTION
import com.example.audioplayer.player.PlayerFragment.Companion.PRESS_PREVIOUS_BUTTON_ACTION
import com.example.audioplayer.player.PlayerFragment.Companion.RECEIVED_TIME
import com.example.audioplayer.player.PlayerFragment.Companion.REPEAT_ACTION
import com.example.audioplayer.player.PlayerFragment.Companion.REPEAT_CONDITION
import com.example.audioplayer.player.PlayerFragment.Companion.SHUFFLE_ACTION
import com.example.audioplayer.player.PlayerFragment.Companion.SHUFFLE_CONDITION
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerService : Service() {

    lateinit var currentSong: Song
    lateinit var mediaPlayer: MediaPlayer
    lateinit var sendedIntent: Intent
    var position: Int = 0
    lateinit var playedSongsList: List<Song>
    lateinit var savedSongsList: ArrayList<Song>
    var shuffleFlag = false
    var repeatFlag = false
    lateinit var notifyManager: NotificationManager
    lateinit var notifBuilder: Notification

    private val binder = MyBinder()
    override fun onBind(intent: Intent?): IBinder? = binder

    override fun onCreate() {
        mediaPlayer = MediaPlayer.create(applicationContext, R.raw.music)
        mediaPlayer.start()
    }

    inner class MyBinder : Binder() {
        fun getService(): PlayerService = this@PlayerService
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        initManager()

        when (intent?.action) {
            PLAY_OR_STOP_FROM_NOTIFY -> {
                playOrPause()
                buildAndShowNotify()
            }
            PLAY_NEXT_FROM_NOTIFY -> {
                if (position < playedSongsList.size - 1) {
                    position++
                    currentSong = playedSongsList[position]
                    playSong(playedSongsList[position])
                    sendInfoForTV()
                    buildAndShowNotify()
                }
            }
            PLAY_PREVIOUS_FROM_NOTIFY -> {
                if (position > 0) {
                    position--
                    currentSong = playedSongsList[position]
                    playSong(playedSongsList[position])
                    sendInfoForTV()
                    buildAndShowNotify()
                }
            }
            PRESS_NEXT_BUTTON_ACTION -> {
                if (position < playedSongsList.size - 1) {
                    position++
                    currentSong = playedSongsList[position]
                    playSong(playedSongsList[position])
                    sendInfoForTV()
                    buildAndShowNotify()
                }
            }
            PRESS_PREVIOUS_BUTTON_ACTION -> {
                if (position > 0) {
                    position--
                    currentSong = playedSongsList[position]
                    playSong(playedSongsList[position])
                    sendInfoForTV()
                    buildAndShowNotify()
                }
            }
            SHUFFLE_ACTION -> {
                if (shuffleFlag) {
                    val savedCurrentPosition = mediaPlayer.currentPosition
                    shuffleFlag = false
                    playedSongsList = savedSongsList
                    position = playedSongsList.indexOf(currentSong)
                    playSong(currentSong)
                    mediaPlayer.seekTo(savedCurrentPosition)
                    sendBroadcast(Intent(SHUFFLE_ACTION).putExtra(SHUFFLE_CONDITION, shuffleFlag))
                } else {
                    val savedCurrentPosition = mediaPlayer.currentPosition
                    shuffleFlag = true
                    val mixedSongsList = savedSongsList as MutableList<Song>
                    playedSongsList = mixedSongsList.shuffled() as ArrayList<Song>
                    (playedSongsList as ArrayList<Song>).remove(currentSong)
                    (playedSongsList as ArrayList<Song>).add(0, currentSong)
                    position = 0
                    playSong(currentSong)
                    mediaPlayer.seekTo(savedCurrentPosition)
                    sendBroadcast(Intent(SHUFFLE_ACTION).putExtra(SHUFFLE_CONDITION, shuffleFlag))
                }
            }
            REPEAT_ACTION -> {
                if (repeatFlag) {
                    repeatFlag = false
                    setNewOnCompletionListener(currentSong, mediaPlayer.currentPosition)
                    sendBroadcast(Intent(REPEAT_ACTION).putExtra(REPEAT_CONDITION, repeatFlag))
                } else {
                    repeatFlag = true
                    setNewOnCompletionListener(currentSong, mediaPlayer.currentPosition)
                    sendBroadcast(Intent(REPEAT_ACTION).putExtra(REPEAT_CONDITION, repeatFlag))
                }
            }
        }

        if (intent?.hasExtra(CURRENT_SONG)!! && intent.hasExtra(LIST_SONGS)) {
            currentSong = intent.getParcelableExtra(CURRENT_SONG)!!
            playedSongsList = intent.getParcelableArrayListExtra(LIST_SONGS)!!
            position = playedSongsList.indexOf(currentSong)
            if (repeatFlag) {
                sendBroadcast(Intent(REPEAT_ACTION).putExtra(REPEAT_CONDITION, repeatFlag))
            } else {
                sendBroadcast(Intent(REPEAT_ACTION).putExtra(REPEAT_CONDITION, repeatFlag))
            }
            shuffleFlag = false
            playSong(currentSong)
            if (intent.hasExtra(GIVED_CURRENT_POSITION)){
                val currentPosition = intent.getIntExtra(GIVED_CURRENT_POSITION, 0)
                mediaPlayer.seekTo(currentPosition)
            }
            buildAndShowNotify()
        }

        if (intent?.hasExtra(CURRENT_SONG)!! && !intent.hasExtra(LIST_SONGS)) {
            currentSong = intent.getParcelableExtra(CURRENT_SONG)!!
            position = playedSongsList.indexOf(currentSong)
            playSong(currentSong)
            sendInfoForTV()
            sendTimeForSB()
            buildAndShowNotify()
        }

        if (intent?.hasExtra(PlayerFragment.SEEKBAR_ACTION)) {
            mediaPlayer.seekTo(intent?.getIntExtra(PlayerFragment.SEEKBAR_ACTION, 0))
        }

        sendTimeForSB()

        return START_STICKY
    }

    fun playOrPause() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
        } else {
            mediaPlayer.start()
        }
    }

    fun playSong(song: Song) {
        mediaPlayer.stop()
        mediaPlayer = MediaPlayer.create(applicationContext, song.uri.toUri())
        setNewOnCompletionListener(song, 0)
        mediaPlayer.start()
        buildAndShowNotify()
    }

    fun setNewOnCompletionListener(song: Song, currentPosition: Int) {
        if (repeatFlag) {
            mediaPlayer.setOnCompletionListener {
                mediaPlayer.stop()
                mediaPlayer = MediaPlayer.create(applicationContext, song.uri.toUri())
                mediaPlayer.start()
                sendInfoForTV()
                sendTimeForSB()
            }
        } else {
            mediaPlayer.setOnCompletionListener {
                if (position < playedSongsList.size - 1) {
                    position++
                    currentSong = playedSongsList[position]
                    playSong(playedSongsList[position])
                    sendInfoForTV()
                    sendTimeForSB()
                } else {
                    position = 0
                    currentSong = playedSongsList[position]
                    playSong(playedSongsList[position])
                    sendInfoForTV()
                    sendTimeForSB()
                }
            }
            mediaPlayer.seekTo(currentPosition)
        }
    }

    fun sendInfoForTV() {
        sendedIntent = Intent(BROADCAST_ACTION_FOR_TV)
            .putExtra(CURRENT_SONG_INFO, playedSongsList[position])
        sendBroadcast(sendedIntent)
    }

    fun sendTimeForSB() {
        sendedIntent = Intent(BROADCAST_ACTION_FOR_SB)
        CoroutineScope(Dispatchers.IO).launch {
            while (mediaPlayer != null) {
                sendedIntent.putExtra(RECEIVED_TIME, mediaPlayer.currentPosition)
                sendBroadcast(sendedIntent)
                delay(1000)
            }
        }
    }

    fun initManager() {
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

    fun buildAndShowNotify() {
        CoroutineScope(Dispatchers.IO).launch {
            while (mediaPlayer!=null) {
                val pendIntForStartPlayer = PendingIntent.getActivity(
                    applicationContext, 0, Intent(applicationContext, MainActivity::class.java)
                        .setAction(START_PLAYER_FRAGMENT_ACTION)
                        .putExtra(PLAYED_LIST, (playedSongsList as ArrayList<Song>))
                        .putExtra(SONG_FROM_NOTIFY, currentSong)
                        .putExtra(CURRENT_POSITION_FROM_NOTIFY, mediaPlayer.currentPosition)
                    , PendingIntent.FLAG_UPDATE_CURRENT
                )
                notifBuilder =
                    NotificationCompat.Builder(
                        applicationContext,
                        PlayerFragment.NOTIFICATION_CHANNEL_ID
                    ).apply {
                        setSmallIcon(R.drawable.ic_baseline_music_note_24)
                        setContentTitle(playedSongsList[position].title)
                        setContentText(playedSongsList[position].subtitle)
                        setContentIntent(pendIntForStartPlayer)
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
                        if (mediaPlayer.isPlaying) {
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
    }

    companion object {
        const val START_PLAYER_FRAGMENT_ACTION = "START_PLAYER_FRAGMENT_ACTION"
        const val PLAYED_LIST = "PLAYED_LIST"
        const val SONG_FROM_NOTIFY = "SONG_FROM_NOTIFY"
        const val CURRENT_POSITION_FROM_NOTIFY = "CURRENT_POSITION_FROM_NOTIFY"
    }
}