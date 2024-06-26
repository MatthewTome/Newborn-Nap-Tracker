package com.example.newbornnaptracker

import android.media.MediaPlayer
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.newbornnaptracker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), MusicPlayerControlListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private var mediaPlayer: MediaPlayer? = null

    private lateinit var albumArtMini: ImageView
    private lateinit var songNameMini: TextView
    private lateinit var artistNameMini: TextView
    private lateinit var playPauseButtonMini: ImageButton

    private var songSelected: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.HomeFragment, R.id.SleepCycleFragment, R.id.AddNapFragment, R.id.SongListFragment)
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.bottomNavigation.setupWithNavController(navController)

        mediaPlayer = MediaPlayer.create(this, R.raw.lullaby)

        // Initialize mini player controls
        albumArtMini = findViewById(R.id.album_art_mini)
        songNameMini = findViewById(R.id.song_name_mini)
        artistNameMini = findViewById(R.id.artist_name_mini)
        playPauseButtonMini = findViewById(R.id.play_pause_button_mini)

        playPauseButtonMini.setOnClickListener {
            if (isPlaying()) {
                pauseSound()
                playPauseButtonMini.setImageResource(R.drawable.ic_play)
            } else {
                playSound()
                playPauseButtonMini.setImageResource(R.drawable.ic_pause)
            }
        }

        // Navigate to the full music player when the mini player is clicked
        findViewById<View>(R.id.music_player_controls).setOnClickListener {
            navController.navigate(R.id.MusicPlayerFragment)
        }

        // Add a listener for fragment changes
        navController.addOnDestinationChangedListener { _, destination, _ ->
            updateMiniPlayerVisibility(destination.id)
            updateNavigationBarVisibility(destination.id)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
    }

    // MusicPlayerControlListener methods
    override fun playSound() {
        mediaPlayer?.let {
            if (!it.isPlaying) {
                it.start()
            }
        }
    }

    override fun pauseSound() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.pause()
            }
        }
    }

    override fun stopSound() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.stop()
                it.prepare()
            }
        }
    }

    override fun setSound(resourceId: Int) {
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer.create(this, resourceId)
        songSelected = true
        updateMiniPlayer(resourceId)

        // Notify MusicPlayerFragment to update the song info
        val musicPlayerFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main)
            ?.childFragmentManager?.fragments?.firstOrNull { it is MusicPlayerFragment } as? MusicPlayerFragment
        musicPlayerFragment?.updateSongInfo(resourceId)
    }

    override fun isPlaying(): Boolean {
        return mediaPlayer?.isPlaying == true
    }

    override fun seekTo(position: Int) {
        mediaPlayer?.seekTo(position)
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayer?.currentPosition ?: 0
    }

    override fun getDuration(): Int {
        return mediaPlayer?.duration ?: 0
    }

    private fun updateMiniPlayer(resourceId: Int) {
        val song = when (resourceId) {
            R.raw.lullaby -> Song("Lullaby", "Artist 1", R.raw.lullaby)
            R.raw.rain -> Song("Rain", "Artist 2", R.raw.rain)
            else -> Song("Unknown", "Unknown Artist", 0)
        }
        songNameMini.text = song.name
        artistNameMini.text = song.artist
        albumArtMini.setImageResource(
            when (song.resourceId) {
                R.raw.lullaby -> R.drawable.lullaby
                R.raw.rain -> R.drawable.rain
                else -> R.drawable.ic_music_note
            }
        )
    }

    fun showMiniPlayer() {
        findViewById<View>(R.id.music_player_controls).visibility = View.VISIBLE
    }

    private fun updateMiniPlayerVisibility(currentFragmentId: Int) {
        val shouldShowMiniPlayer = songSelected && currentFragmentId != R.id.MusicPlayerFragment
        findViewById<View>(R.id.music_player_controls).visibility = if (shouldShowMiniPlayer) View.VISIBLE else View.GONE
    }

    private fun updateNavigationBarVisibility(currentFragmentId: Int) {
        val navBar = findViewById<View>(R.id.bottom_navigation)
        navBar.visibility = if (currentFragmentId == R.id.MusicPlayerFragment) View.GONE else View.VISIBLE
    }
}