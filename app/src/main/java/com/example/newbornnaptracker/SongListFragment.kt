package com.example.newbornnaptracker

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SongListFragment : Fragment() {

    private lateinit var songList: RecyclerView
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
        val view = inflater.inflate(R.layout.fragment_song_list, container, false)
        songList = view.findViewById(R.id.song_list)
        songList.layoutManager = LinearLayoutManager(context)
        songList.adapter = SongAdapter(getSongs(), listener)
        return view
    }

    private fun getSongs(): List<Song> {
        return listOf(
            Song("Lullaby", "Artist 1", R.raw.lullaby),
            Song("Rain", "Artist 2", R.raw.rain)
        )
    }
}