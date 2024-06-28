package com.example.newbornnaptracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SoundAdapter(
    private val sounds: List<Sound>,
    private val listener: SoundMachineControlListener?
) : RecyclerView.Adapter<SoundAdapter.SoundViewHolder>() {

    inner class SoundViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val albumArt: ImageView = itemView.findViewById(R.id.album_art_item)
        val soundName: TextView = itemView.findViewById(R.id.sound_name_item)
        val artistName: TextView = itemView.findViewById(R.id.artist_name_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SoundViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.sound_item, parent, false)
        return SoundViewHolder(view)
    }

    override fun onBindViewHolder(holder: SoundViewHolder, position: Int) {
        val sound = sounds[position]
        holder.soundName.text = sound.name
        holder.artistName.text = sound.artist
        holder.albumArt.setImageResource(
            when (sound.resourceId) {
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
            listener?.setSound(sound.resourceId)
            listener?.playSound()
            (listener as? MainActivity)?.showMiniPlayer()
        }
    }

    override fun getItemCount() = sounds.size
}