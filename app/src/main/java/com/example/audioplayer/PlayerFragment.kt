package com.example.audioplayer

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.*
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import com.example.audioplayer.SongsListFragment.Companion.CLICKED_SONG_KEY
import com.example.audioplayer.SongsListFragment.Companion.CLICKED_SONG_POSITION_KEY
import com.example.audioplayer.databinding.FragmentPlayerBinding

class PlayerFragment : Fragment() {

    private var binding: FragmentPlayerBinding? = null
    var managerIntent = Intent()
    var duration: Int? = 0
    var usedService = PlayerService()
    var position: Int = 0
    lateinit var songList: MutableList<Song>
    lateinit var notifBuilder: Notification

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.arguments = (activity as MainActivity).bundle
        val song = (arguments?.get(CLICKED_SONG_KEY) as Song)
        binding?.tvSongTitle?.text = song.title
        binding?.tvSongSubtitle?.text = song.subtitle
        position = arguments?.getInt(CLICKED_SONG_POSITION_KEY)!!
        songList = FileRepository.songList

        managerIntent = Intent(view.context, PlayerService::class.java).putExtra(CURRENT_SONG, song)
        activity?.startService(managerIntent)
        playOrStopMusic()

        val serviceConnection = object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                usedService = (service as PlayerService.MyBinder).getService()
                updatePlayButton()
                duration = usedService.mediaPlayer.duration
                binding!!.sbMusicChanges.max = duration!!
            }
            override fun onServiceDisconnected(name: ComponentName?) {}
        }
        activity?.bindService(managerIntent, serviceConnection, 0)
        nextAndPreviousButtonsAction()

        val brForSeekBar = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val currentTime = intent?.getIntExtra(RECEIVED_TIME, 0)
                updateSeekBar(currentTime!!)
                updatePlayButton()
            }
        }
        activity?.registerReceiver(brForSeekBar, IntentFilter(BROADCAST_ACTION_FOR_SB))

        seekBarAction()

        val notifyManager =
            activity?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (notifyManager.getNotificationChannel(NOTIFICATION_CHANNEL_ID) == null) {
                val channel = NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    NOTIF_CHAR_SEQUENCE,
                    NotificationManager.IMPORTANCE_DEFAULT
                )
                notifyManager.createNotificationChannel(channel)
            }
        }

//        val remoteViews = RemoteViews(activity?.packageName, R.layout.notification_player)
//        remoteViews.setTextViewText(R.id.tv_notify_song_title, "Song Title From Fragment")
//        remoteViews.setTextViewText(R.id.tv_notify_song_subtitle, "Song Subtitle From Fragment")
//        remoteViews.setOnClickPendingIntent(R.id.bv_notify_play_or_stop_track,
//            PendingIntent.getService(activity?.applicationContext,
//                0,
//                Intent(activity?.applicationContext, PlayerService::class.java),
//                0))

        val playIntent = Intent(view.context, PlayerService::class.java)
        playIntent.action = PLAY_OR_STOP_ACTION_FROM_NOTIFY
        val playPendInt = PendingIntent.getService(view.context, 0, playIntent, 0)

        notifBuilder =
            NotificationCompat.Builder(activity?.applicationContext!!, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_music_note_24)
                .setContentTitle(song.title)
                .setContentText(song.subtitle)
                .addAction(R.drawable.ic_baseline_play_arrow_24, "Play", playPendInt)
                .build()
        notifyManager.notify(NOTIFICATION_ID, notifBuilder)
    }



    fun updateSeekBar(currentTime: Int) {
        binding!!.sbMusicChanges.progress = currentTime
        updateTVWithTime(currentTime)
    }

    fun updateTVWithTime(currentTime: Int){
        binding!!.tvWastedTime.text = createTimeLabel(currentTime)
        binding!!.tvRemainingTime.text = createTimeLabel(duration!! - currentTime)
    }

    fun updatePlayButton() {
        if (usedService.mediaPlayer.isPlaying) {
            binding!!.bvPlay.setBackgroundResource(R.drawable.ic_baseline_pause_24)
        } else {
            binding!!.bvPlay.setBackgroundResource(R.drawable.ic_baseline_play_arrow_24)
        }
    }

    fun createTimeLabel(time: Int): String {
        var timeLabel = ""
        val min = time / 1000 / 60
        val sec = time / 1000 % 60

        timeLabel = "$min"
        if (sec < 10) {
            timeLabel += ":0$sec"
        } else {
            timeLabel += ":$sec"
        }

        return timeLabel
    }

    fun playOrStopMusic() {
        binding!!.bvPlay.setOnClickListener {
            usedService.playOrStopMusic()
            updatePlayButton()
        }
    }

    fun seekBarAction() {
        binding!!.sbMusicChanges.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    managerIntent = Intent(activity?.applicationContext, PlayerService::class.java)
                        .putExtra(SEEKBAR_ACTION, progress)
                    activity?.startService(managerIntent)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    fun nextAndPreviousButtonsAction(){
        binding?.bvPlayNextTrack?.setOnClickListener {
            if (position<songList.size-1){
                position++
                managerIntent = Intent(activity?.applicationContext, PlayerService::class.java).putExtra(CURRENT_SONG, songList[position])
                activity?.startService(managerIntent)
                updateTitleAndSubtitle(position)
                duration = usedService.mediaPlayer.duration
                binding!!.sbMusicChanges.max = duration!!
                updateTVWithTime(usedService.mediaPlayer.currentPosition)
            }
        }
        binding?.bvPlayLastTrack?.setOnClickListener {
            if (position>0){
                position--
                managerIntent = Intent(activity?.applicationContext, PlayerService::class.java).putExtra(CURRENT_SONG, songList[position])
                activity?.startService(managerIntent)
                updateTitleAndSubtitle(position)
                duration = usedService.mediaPlayer.duration
                binding!!.sbMusicChanges.max = duration!!
                updateTVWithTime(usedService.mediaPlayer.currentPosition)
            }
        }
    }

    fun updateTitleAndSubtitle(position: Int){
        binding?.tvSongTitle?.text = songList[position].title
        binding?.tvSongSubtitle?.text = songList[position].subtitle
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    companion object {
        const val PLAYER_FRAGMENT_TAG = "PLAYER_FRAGMENT_TAG"

        const val SEEKBAR_ACTION = "seekBarAction"

        const val RECEIVED_TIME = "receivedTime"
        const val BROADCAST_ACTION_FOR_SB = "broadcastActionForSB"

        const val NOTIFICATION_CHANNEL_ID = "SOME_CHANNEL"
        const val NOTIFICATION_ID = 0
        const val NOTIF_CHAR_SEQUENCE = "Player notify"
        const val PLAY_OR_STOP_ACTION_FROM_NOTIFY = "PLAY_OR_STOP_ACTION_FROM_NOTIFY"

        const val CURRENT_SONG = "CURRENT_SONG"
    }
}