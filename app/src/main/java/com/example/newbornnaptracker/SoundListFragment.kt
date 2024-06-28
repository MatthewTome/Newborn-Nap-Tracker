package com.example.newbornnaptracker

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SoundListFragment : Fragment() {

    private lateinit var soundList: RecyclerView
    private var listener: SoundMachineControlListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is SoundMachineControlListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement SoundMachineControlListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sound_list, container, false)
        soundList = view.findViewById(R.id.sound_list)
        soundList.layoutManager = LinearLayoutManager(context)
        soundList.adapter = SoundAdapter(getSounds(), listener)
        return view
    }

    private fun getSounds(): List<Sound> {
        return listOf(
            Sound("Lullaby", "Sleep for Babies", R.raw.lullaby),
            Sound("Rain", "Soothing Sounds", R.raw.rain),
            Sound("Piano Sleep Music", "Sleep for Babies", R.raw.pianosleepmusic),
            Sound("Lullaby Calming Piano", "Sleep for Babies", R.raw.lullabycalmingpiano),
            Sound("Sweet Dreams", "Sleep for Babies", R.raw.sweetdreams),
            Sound("Sleep Lullaby", "Sleep for Babies", R.raw.sleeplullaby)
        )
    }
}