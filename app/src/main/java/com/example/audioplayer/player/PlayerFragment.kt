package com.example.audioplayer.player

import android.content.*
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import com.example.audioplayer.R
import com.example.audioplayer.baseEntities.Song
import com.example.audioplayer.databinding.FragmentPlayerBinding
import com.example.audioplayer.player.PlayerService.Companion.CURRENT_POSITION_FROM_NOTIFY
import com.example.audioplayer.player.dialog.DialogForPlayer

class PlayerFragment(
    val songsList: ArrayList<Song>?,
    val firstClickedSong: Song?,
    val currentPosition: Int
) : Fragment() {

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

        updateTitleAndSubtitle(firstClickedSong!!)

        managerIntent = Intent(view.context, PlayerService::class.java)
        managerIntent.putExtra(CURRENT_SONG, firstClickedSong)
        managerIntent.putExtra(LIST_SONGS, songsList)
        managerIntent.putExtra(GIVED_CURRENT_POSITION, currentPosition)
        activity?.startService(managerIntent)
        playOrStopMusic()

        val serviceConnection = object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                usedService = (service as PlayerService.MyBinder).getService()
                usedService.savedSongsList = songsList!!
                updatePlayButton()
                duration = usedService.mediaPlayer.duration
                binding?.sbMusicChanges?.max = duration!!
//                val audioSession = usedService.mediaPlayer.audioSessionId
//                if (audioSession!=-1){
//                    binding?.blast?.setAudioSessionId(audioSession)
//                }
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
                val receivedSong = intent?.getParcelableExtra<Song>(CURRENT_SONG_INFO) as Song
                updateTitleAndSubtitle(receivedSong)
                duration = receivedSong.duration
                binding?.sbMusicChanges?.max = duration!!
            }
        }
        activity?.registerReceiver(brForTV, IntentFilter(BROADCAST_ACTION_FOR_TV))

        val brForRepeatBtn = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val repeatBtnCondition = intent?.getBooleanExtra(REPEAT_CONDITION, false)
                if (repeatBtnCondition!!){
                    binding?.bvRepeatingSongs?.setBackgroundResource(R.drawable.exo_controls_repeat_one)
                } else {
                    binding?.bvRepeatingSongs?.setBackgroundResource(R.drawable.exo_controls_repeat_off)
                }
            }
        }
        activity?.registerReceiver(brForRepeatBtn, IntentFilter(REPEAT_ACTION))

        val brForShuffleBtn = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val shuffleBtnCondition = intent?.getBooleanExtra(SHUFFLE_CONDITION, false)
                if (shuffleBtnCondition!!){
                    binding?.bvMixSongs?.setBackgroundResource(R.drawable.exo_controls_shuffle_on)
                } else {
                    binding?.bvMixSongs?.setBackgroundResource(R.drawable.exo_controls_shuffle_off)
                }
            }
        }
        activity?.registerReceiver(brForShuffleBtn, IntentFilter(SHUFFLE_ACTION))

        seekBarAction()
        bvMixSongsActions()
        bvRepeatActions()
        bvSongsList()
        bvToBackAction()
    }

    fun bvMixSongsActions(){
        binding?.bvMixSongs?.setOnClickListener {
            managerIntent = Intent(activity?.applicationContext, PlayerService::class.java)
            managerIntent.action = SHUFFLE_ACTION
            activity?.startService(managerIntent)
        }
    }

    fun bvRepeatActions(){
        binding?.bvRepeatingSongs?.setOnClickListener {
            managerIntent = Intent(activity?.applicationContext, PlayerService::class.java)
            managerIntent.action = REPEAT_ACTION
            activity?.startService(managerIntent)
        }
    }

    fun bvSongsList(){
        binding?.bvSongsList?.setOnClickListener {
            DialogForPlayer((usedService.playedSongsList as ArrayList<Song>), {pathSongFromDialog(it)}).show(parentFragmentManager, PLAYER_DIALOG_TAG)
        }
    }

    fun bvToBackAction(){
        binding?.bvToBack?.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    fun pathSongFromDialog(song: Song){
        managerIntent = Intent(activity?.applicationContext, PlayerService::class.java)
        managerIntent.putExtra(CURRENT_SONG, song)
        activity?.startService(managerIntent)
    }

    fun updateSeekBar(currentTime: Int) {
        binding?.sbMusicChanges?.progress = currentTime
        updateTVWithTime(currentTime)
    }

    fun updateTVWithTime(currentTime: Int) {
        binding?.tvWastedTime?.text = createTimeLabel(currentTime)
        binding?.tvRemainingTime?.text = createTimeLabel(duration!! - currentTime)
    }

    fun updatePlayButton() {
        if (usedService.mediaPlayer.isPlaying) {
            binding?.bvPlay?.setBackgroundResource(R.drawable.ic_baseline_pause_24)
        } else {
            binding?.bvPlay?.setBackgroundResource(R.drawable.ic_baseline_play_arrow_24)
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
        binding?.bvPlay?.setOnClickListener {
            usedService.playOrPause()
            updatePlayButton()
        }
    }

    fun seekBarAction() {
        binding?.sbMusicChanges?.setOnSeekBarChangeListener(object :
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
        const val REPEAT_CONDITION = "REPEAT_CONDITION"
        const val SHUFFLE_CONDITION = "SHUFFLE_CONDITION"

        const val LIST_SONGS = "LIST_SONGS"

        const val SHUFFLE_ACTION = "SHUFFLE_ACTION"
        const val REPEAT_ACTION = "REPEAT_ACTION"

        const val PLAYER_DIALOG_TAG = "PLAYER_DIALOG_TAG"
        const val PLAYER_FRAGMENT_TAG = "PLAYER_FRAGMENT_TAG"
        const val GIVED_CURRENT_POSITION = "GIVED_CURRENT_POSITION"
    }
}