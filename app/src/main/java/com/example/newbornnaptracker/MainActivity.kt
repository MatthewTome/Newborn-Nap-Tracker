package com.example.newbornnaptracker

import android.graphics.drawable.GradientDrawable
import android.media.MediaPlayer
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.newbornnaptracker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), SoundMachineControlListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private var mediaPlayer: MediaPlayer? = null
    private lateinit var albumArtMini: ImageView
    private lateinit var soundNameMini: TextView
    private lateinit var artistNameMini: TextView
    private lateinit var playPauseButtonMini: ImageButton
    private var soundSelected: Boolean = false

    private val sharedViewModel: SharedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.HomeFragment, R.id.SleepCycleFragment, R.id.AddNapFragment, R.id.SoundListFragment)
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.bottomNavigation.setupWithNavController(navController)

        mediaPlayer = MediaPlayer.create(this, R.raw.lullaby)

        albumArtMini = findViewById(R.id.album_art_mini)
        soundNameMini = findViewById(R.id.sound_name_mini)
        artistNameMini = findViewById(R.id.artist_name_mini)
        playPauseButtonMini = findViewById(R.id.play_pause_button_mini)

        playPauseButtonMini.setOnClickListener {
            if (isPlaying()) {
                pauseSound()
                sharedViewModel.setPlaying(false)
            } else {
                playSound()
                sharedViewModel.setPlaying(true)
            }
        }

        findViewById<View>(R.id.sound_machine_controls).setOnClickListener {
            navController.navigate(R.id.MusicPlayerFragment)
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            updateMiniPlayerVisibility(destination.id)
            updateNavigationBarVisibility(destination.id)
        }

        sharedViewModel.isPlaying.observe(this) { isPlaying ->
            if (isPlaying) {
                playPauseButtonMini.setImageResource(R.drawable.ic_pause)
            } else {
                playPauseButtonMini.setImageResource(R.drawable.ic_play)
            }
        }

        // Initialize the play/pause button state
        sharedViewModel.setPlaying(true)

        // Create a GradientDrawable
        val gradient = GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM,
            intArrayOf(0xFF030003.toInt(), 0xFF0A010C.toInt())
        )
        gradient.setGradientCenter(0.5f, 0.9f)
        val layout = findViewById<CoordinatorLayout>(R.id.main_layout)
        layout.background = gradient
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.SettingsFragment)
                true
            }
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
        soundSelected = true
        updateMiniPlayer(resourceId)
        sharedViewModel.setResourceId(resourceId)
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
        val sound = when (resourceId) {
            R.raw.lullaby -> Sound("Lullaby", "Sleep For Babies", R.raw.lullaby)
            R.raw.rain -> Sound("Rain", "Soothing Sounds", R.raw.rain)
            R.raw.pianosleepmusic -> Sound("Piano Sleep Music", "Sleep for Babies", R.raw.pianosleepmusic)
            R.raw.lullabycalmingpiano -> Sound("Lullaby Calming Piano", "Sleep for Babies", R.raw.lullabycalmingpiano)
            R.raw.sweetdreams -> Sound("Sweet Dreams", "Sleep for Babies", R.raw.sweetdreams)
            R.raw.sleeplullaby -> Sound("Sleep Lullaby", "Sleep for Babies", R.raw.sleeplullaby)
            else -> Sound("Unknown", "Unknown Artist", 0)
        }
        soundNameMini.text = sound.name
        artistNameMini.text = sound.artist
        albumArtMini.setImageResource(
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
    }

    fun showMiniPlayer() {
        findViewById<View>(R.id.sound_machine_controls).visibility = View.VISIBLE
    }

    private fun updateMiniPlayerVisibility(currentFragmentId: Int) {
        val shouldShowMiniPlayer = soundSelected && currentFragmentId != R.id.MusicPlayerFragment
        findViewById<View>(R.id.sound_machine_controls).visibility = if (shouldShowMiniPlayer) View.VISIBLE else View.GONE
    }

    private fun updateNavigationBarVisibility(currentFragmentId: Int) {
        val navBar = findViewById<View>(R.id.bottom_navigation)
        navBar.visibility = if (currentFragmentId == R.id.MusicPlayerFragment) View.GONE else View.VISIBLE
    }
}