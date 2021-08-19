package com.example.audioplayer

import android.content.*
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import com.example.audioplayer.allSongList.SongsListFragment.Companion.CLICKED_SONG_KEY
import com.example.audioplayer.baseEntities.Song
import com.example.audioplayer.databinding.FragmentPlayerBinding

class PlayerFragment : Fragment() {

    private var binding: FragmentPlayerBinding? = null
    var managerIntent = Intent()
    var duration: Int? = 0
    var usedService = PlayerService()

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
        updateTitleAndSubtitle(song)

        managerIntent = Intent(view.context, PlayerService::class.java)
        managerIntent.putExtra(CURRENT_SONG, song)
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

        val brForTV = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val receivedSong = intent?.getSerializableExtra(CURRENT_SONG_INFO) as Song
                updateTitleAndSubtitle(receivedSong)
                duration = receivedSong.duration
                binding!!.sbMusicChanges.max = duration!!
            }
        }
        activity?.registerReceiver(brForTV, IntentFilter(BROADCAST_ACTION_FOR_TV))

        seekBarAction()
    }


    fun updateSeekBar(currentTime: Int) {
        binding!!.sbMusicChanges.progress = currentTime
        updateTVWithTime(currentTime)
    }

    fun updateTVWithTime(currentTime: Int) {
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
            usedService.playOrPause()
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

    fun nextAndPreviousButtonsAction() {
        binding?.bvPlayNextTrack?.setOnClickListener {
            managerIntent = Intent(activity?.applicationContext, PlayerService::class.java)
                .setAction(PRESS_NEXT_BUTTON_ACTION)
            activity?.startService(managerIntent)
        }
        binding?.bvPlayLastTrack?.setOnClickListener {
            managerIntent = Intent(activity?.applicationContext, PlayerService::class.java)
                .setAction(PRESS_PREVIOUS_BUTTON_ACTION)
            activity?.startService(managerIntent)
        }
    }

    fun updateTitleAndSubtitle(song: Song) {
        binding?.tvSongTitle?.text = song.title
        binding?.tvSongSubtitle?.text = song.subtitle
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    companion object {
        const val SEEKBAR_ACTION = "seekBarAction"

        const val RECEIVED_TIME = "receivedTime"
        const val BROADCAST_ACTION_FOR_SB = "broadcastActionForSB"
        const val BROADCAST_ACTION_FOR_TV = "BROADCAST_ACTION_FOR_TV"
        const val PRESS_NEXT_BUTTON_ACTION = "PRESS_NEXT_BUTTON_ACTION"
        const val PRESS_PREVIOUS_BUTTON_ACTION = "PRESS_PREVIOUS_BUTTON_ACTION"

        const val NOTIFICATION_CHANNEL_ID = "SOME_CHANNEL"
        const val NOTIFICATION_ID = 0
        const val NOTIF_CHAR_SEQUENCE = "Player notify"
        const val PLAY_OR_STOP_FROM_NOTIFY = "PLAY_OR_STOP_FROM_NOTIFY"
        const val PLAY_NEXT_FROM_NOTIFY = "PLAY_NEXT_FROM_NOTIFY"
        const val PLAY_PREVIOUS_FROM_NOTIFY = "PLAY_PREVIOUS_FROM_NOTIFY"

        const val CURRENT_SONG = "CURRENT_SONG"
        const val CURRENT_SONG_INFO = "CURRENT_SONG_INFO"
    }
}