package com.example.newbornnaptracker

import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.Spinner
import androidx.fragment.app.Fragment

class MusicPlayerFragment : Fragment() {

    private var mediaPlayer: MediaPlayer? = null
    private lateinit var playButton: Button
    private lateinit var pauseButton: Button
    private lateinit var stopButton: Button
    private lateinit var soundSpinner: Spinner

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_music_player, container, false)

        playButton = view.findViewById(R.id.play_button)
        pauseButton = view.findViewById(R.id.pause_button)
        stopButton = view.findViewById(R.id.stop_button)
        soundSpinner = view.findViewById(R.id.sound_spinner)

        playButton.setOnClickListener { playSound() }
        pauseButton.setOnClickListener { pauseSound() }
        stopButton.setOnClickListener { stopSound() }

        soundSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                when (position) {
                    0 -> setSound(R.raw.lullaby)
                    1 -> setSound(R.raw.rain)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }

        return view
    }

    private fun setSound(resourceId: Int) {
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer.create(context, resourceId)
    }

    private fun playSound() {
        mediaPlayer?.let {
            if (!it.isPlaying) {
                it.start()
            }
        }
    }

    private fun pauseSound() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.pause()
            }
        }
    }

    private fun stopSound() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.stop()
                it.prepare()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}