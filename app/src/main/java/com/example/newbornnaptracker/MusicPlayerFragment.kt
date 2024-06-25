package com.example.newbornnaptracker

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.fragment.app.Fragment

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

        // Update UI with song details and seek bar logic

        return view
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    fun updateImage(resourceId: Int) {
        val imageResId = when (resourceId) {
            R.raw.lullaby -> R.drawable.lullaby
            R.raw.rain -> R.drawable.rain
            else -> 0  // Default case, no image
        }
        if (imageResId != 0) {
            albumArt.setImageResource(imageResId)
            // Update mini player with the same album art
            (activity as? MainActivity)?.updateMiniPlayer(resourceId)
        }
    }
}