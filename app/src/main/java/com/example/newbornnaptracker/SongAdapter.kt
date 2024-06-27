package com.example.newbornnaptracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SongAdapter(
    private val songs: List<Song>,
    private val listener: MusicPlayerControlListener?
) : RecyclerView.Adapter<SongAdapter.SongViewHolder>() {

    inner class SongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val albumArt: ImageView = itemView.findViewById(R.id.album_art_item)
        val songName: TextView = itemView.findViewById(R.id.song_name_item)
        val artistName: TextView = itemView.findViewById(R.id.artist_name_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.song_item, parent, false)
        return SongViewHolder(view)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val song = songs[position]
        holder.songName.text = song.name
        holder.artistName.text = song.artist
        holder.albumArt.setImageResource(
            when (song.resourceId) {
                R.raw.lullaby -> R.drawable.lullaby
                R.raw.rain -> R.drawable.rain
                R.raw.pianosleepmusic -> R.drawable.piano_sleep_music
                R.raw.lullabycalmingpiano -> R.drawable.lullaby_calming_piano
                R.raw.sweetdreams -> R.drawable.sweet_dreams
                R.raw.sleeplullaby -> R.drawable.sleep_lullaby
                else -> R.drawable.ic_music_note
            }
        )

        holder.itemView.setOnClickListener {
            listener?.setSound(song.resourceId)
            listener?.playSound()
            (listener as? MainActivity)?.showMiniPlayer()
        }
    }

    override fun getItemCount() = songs.size
}