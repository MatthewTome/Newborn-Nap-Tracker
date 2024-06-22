package com.example.newbornnaptracker

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.Spinner
import androidx.fragment.app.Fragment

class MusicPlayerFragment : Fragment() {

    private lateinit var playButton: Button
    private lateinit var pauseButton: Button
    private lateinit var stopButton: Button
    private lateinit var soundSpinner: Spinner
    private var listener: MusicPlayerControlListener? = null
    private var selectedSoundResourceId: Int = R.raw.lullaby  // Default sound

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

        playButton = view.findViewById(R.id.play_button)
        pauseButton = view.findViewById(R.id.pause_button)
        stopButton = view.findViewById(R.id.stop_button)
        soundSpinner = view.findViewById(R.id.sound_spinner)

        playButton.setOnClickListener { listener?.playSound() }
        pauseButton.setOnClickListener { listener?.pauseSound() }
        stopButton.setOnClickListener { listener?.stopSound() }

        soundSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedSoundResourceId = when (position) {
                    0 -> R.raw.lullaby
                    1 -> R.raw.rain
                    else -> R.raw.lullaby  // Default case
                }
                if (listener?.isPlaying() == false) {
                    listener?.setSound(selectedSoundResourceId)
                } else {
                    listener?.setSound(selectedSoundResourceId)  // Always set the sound to update the image
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }

        return view
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }
}
