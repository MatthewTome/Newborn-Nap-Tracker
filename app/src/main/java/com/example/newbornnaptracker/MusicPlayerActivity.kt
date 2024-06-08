package com.example.newbornnaptracker

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity

class MusicPlayerActivity : AppCompatActivity() {

    private var mediaPlayer: MediaPlayer? = null
    private lateinit var playButton: Button
    private lateinit var pauseButton: Button
    private lateinit var stopButton: Button
    private lateinit var backButton: Button
    private lateinit var soundSpinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music_player)

        playButton = findViewById(R.id.play_button)
        pauseButton = findViewById(R.id.pause_button)
        stopButton = findViewById(R.id.stop_button)
        backButton = findViewById(R.id.back_button)
        soundSpinner = findViewById(R.id.sound_spinner)

        playButton.setOnClickListener { playSound() }
        pauseButton.setOnClickListener { pauseSound() }
        stopButton.setOnClickListener { stopSound() }
        backButton.setOnClickListener { navigateBack() }

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
    }

    private fun setSound(resourceId: Int) {
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer.create(this, resourceId)
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

    private fun navigateBack() {
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}





// MusicService Version WIP below:
//class MusicPlayerActivity : AppCompatActivity() {
//
//    private lateinit var playButton: Button
//    private lateinit var pauseButton: Button
//    private lateinit var stopButton: Button
//    private lateinit var backButton: Button
//    private lateinit var soundSpinner: Spinner
//
//    private var selectedSoundResId: Int = 0
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_music_player)
//
//        playButton = findViewById(R.id.play_button)
//        pauseButton = findViewById(R.id.pause_button)
//        stopButton = findViewById(R.id.stop_button)
//        backButton = findViewById(R.id.back_button)
//        soundSpinner = findViewById(R.id.sound_spinner)
//
//        playButton.setOnClickListener { playSound() }
//        pauseButton.setOnClickListener { pauseSound() }
//        stopButton.setOnClickListener { stopSound() }
//        backButton.setOnClickListener { navigateBack() }
//
//        soundSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
//                selectedSoundResId = when (position) {
//                    0 -> R.raw.lullaby
//                    1 -> R.raw.rain
//                    else -> 0
//                }
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>) {
//                // Do nothing
//            }
//        }
//    }
//
//    private fun playSound() {
//        if (selectedSoundResId != 0) {
//            val intent = Intent(this, MusicService::class.java).apply {
//                action = "PLAY"
//                putExtra("SOUND_RES_ID", selectedSoundResId)
//            }
//            startService(intent)
//        }
//    }
//
//    private fun pauseSound() {
//        // Implement pause functionality if needed
//    }
//
//    private fun stopSound() {
//        val intent = Intent(this, MusicService::class.java).apply {
//            action = "STOP"
//        }
//        startService(intent)
//    }
//
//    private fun navigateBack() {
//        finish()
//    }
//}