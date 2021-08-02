package com.example.audioplayer

import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.audioplayer.databinding.FragmentPlayerBinding
import kotlinx.coroutines.*
import java.lang.Exception
import kotlin.coroutines.coroutineContext
import kotlin.system.measureTimeMillis

class PlayerFragment : Fragment() {

    private var binding: FragmentPlayerBinding? = null
    val playerService = PlayerService()

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
        val pendingIntent = activity?.createPendingResult(PEND_REC_CODE, Intent(), 0)
        val intent = Intent(view.context, PlayerService::class.java).putExtra(PEND_KEY, pendingIntent)
        activity?.startService(intent)
//        var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
//            if (it.resultCode == Activity.RESULT_OK){
//                val data = it.data
//                binding!!.tvSongTitle.text = data?.getStringExtra(RESULT)
//            }
//        }
//
//        resultLauncher.launch()
        //activity?.startService(Intent(view.context, playerService::class.java))
       //binding!!.sbMusicChanges.max = playerService.mediaPlayer.duration
        //playerService.playOrStopMusic()
        //seekBarAction()

//        CoroutineScope(Dispatchers.Main).launch {
//            while (playerService.mediaPlayer!=null){
//                updateSeekBar()
//                delay(1000)
//            }
//        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == PEND_REC_CODE){
            binding!!.tvSongTitle.text = "Some message"
        }

//        binding!!.tvSongTitle.text = data?.getStringExtra(RESULT)
    }

    fun updateSeekBar() {
        binding!!.sbMusicChanges.progress = playerService.mediaPlayer.currentPosition
        binding!!.tvWastedTime.text = createTimeLabel(playerService.mediaPlayer.currentPosition)
        binding!!.tvRemainingTime.text = createTimeLabel(playerService.mediaPlayer.duration - playerService.mediaPlayer.currentPosition)

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

//    fun playOrStopMusic() {
//        binding!!.bvPlay.setOnClickListener {
//            if (playerService.mediaPlayer.isPlaying) {
//                playerService.mediaPlayer.pause()
//                binding!!.bvPlay.setBackgroundResource(R.drawable.ic_baseline_play_arrow_24)
//            } else {
//                playerService.mediaPlayer.start()
//                binding!!.bvPlay.setBackgroundResource(R.drawable.ic_baseline_pause_24)
//            }
//        }
//    }

    fun seekBarAction() {
        binding!!.sbMusicChanges.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser){
                    playerService.mediaPlayer.seekTo(progress);
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    companion object {
        const val PLAYER_FRAGMENT_TAG = "PLAYER_FRAGMENT_TAG"
        const val PEND_REC_CODE = 1
        const val PEND_KEY = "pendingIntent"
        const val RESULT = "result"
    }
}