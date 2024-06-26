package com.example.newbornnaptracker

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import java.util.Locale
import java.util.concurrent.TimeUnit

class MusicPlayerFragment : Fragment() {

    private lateinit var playPauseButton: ImageButton
    private lateinit var prevButton: ImageButton
    private lateinit var nextButton: ImageButton
    private lateinit var seekBar: SeekBar
    private lateinit var songName: TextView
    private lateinit var artistName: TextView
    private lateinit var currentTime: TextView
    private lateinit var totalTime: TextView
    private lateinit var albumArt: ImageView

    private var listener: MusicPlayerControlListener? = null
    private var handler: Handler = Handler(Looper.getMainLooper())

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MusicPlayerControlListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement MusicPlayerControlListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_music_player, container, false)

        playPauseButton = view.findViewById(R.id.play_pause_button)
        prevButton = view.findViewById(R.id.prev_button)
        nextButton = view.findViewById(R.id.next_button)
        seekBar = view.findViewById(R.id.seek_bar)
        songName = view.findViewById(R.id.song_name)
        artistName = view.findViewById(R.id.artist_name)
        currentTime = view.findViewById(R.id.current_time)
        totalTime = view.findViewById(R.id.total_time)
        albumArt = view.findViewById(R.id.album_art)

        playPauseButton.setOnClickListener {
            if (listener?.isPlaying() == true) {
                listener?.pauseSound()
                playPauseButton.setImageResource(R.drawable.ic_play)
            } else {
                listener?.playSound()
                playPauseButton.setImageResource(R.drawable.ic_pause)
            }
        }

        prevButton.setOnClickListener {
            // Handle previous button click
        }

        nextButton.setOnClickListener {
            // Handle next button click
        }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    listener?.seekTo(progress)
                    updateCurrentTime(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        updateUI()

        return view
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    private fun updateUI() {
        handler.postDelayed(object : Runnable {
            override fun run() {
                listener?.let {
                    val currentPosition = it.getCurrentPosition()
                    seekBar.progress = currentPosition
                    updateCurrentTime(currentPosition)
                    if (it.isPlaying()) {
                        playPauseButton.setImageResource(R.drawable.ic_pause)
                    } else {
                        playPauseButton.setImageResource(R.drawable.ic_play)
                    }
                }
                handler.postDelayed(this, 1000)
            }
        }, 1000)
    }

    private fun updateCurrentTime(progress: Int) {
        val minutes = TimeUnit.MILLISECONDS.toMinutes(progress.toLong())
        val seconds = TimeUnit.MILLISECONDS.toSeconds(progress.toLong()) -
                TimeUnit.MINUTES.toSeconds(minutes)
        currentTime.text = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
    }

    fun updateSongInfo(resourceId: Int) {
        val song = when (resourceId) {
            R.raw.lullaby -> Song("Lullaby", "Artist 1", R.raw.lullaby)
            R.raw.rain -> Song("Rain", "Artist 2", R.raw.rain)
            else -> Song("Unknown", "Unknown Artist", 0)
        }
        songName.text = song.name
        artistName.text = song.artist
        albumArt.setImageResource(
            when (song.resourceId) {
                R.raw.lullaby -> R.drawable.lullaby
                R.raw.rain -> R.drawable.rain
                else -> R.drawable.ic_music_note
            }
        )
        seekBar.max = listener?.getDuration() ?: 0
        totalTime.text = String.format(Locale.getDefault(), "%02d:%02d",
            TimeUnit.MILLISECONDS.toMinutes(seekBar.max.toLong()),
            TimeUnit.MILLISECONDS.toSeconds(seekBar.max.toLong()) -
                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(seekBar.max.toLong()))
        )
    }
}
