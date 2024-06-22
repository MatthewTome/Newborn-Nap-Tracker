package com.example.newbornnaptracker

import android.media.MediaPlayer
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
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
    private lateinit var imageView: ImageView
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.SleepCycleFragment, R.id.AddNapFragment, R.id.MusicPlayerFragment)
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.bottomNavigation.setupWithNavController(navController)

        imageView = findViewById(R.id.imageView)
        mediaPlayer = MediaPlayer.create(this, R.raw.lullaby)
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
        updateImage(resourceId)
    }

    override fun isPlaying(): Boolean {
        return mediaPlayer?.isPlaying == true
    }

    private fun updateImage(resourceId: Int) {
        val imageResId = when (resourceId) {
            R.raw.lullaby -> R.drawable.lullaby
            R.raw.rain -> R.drawable.rain
            else -> 0  // Default case, no image
        }
        if (imageResId != 0) {
            imageView.setImageResource(imageResId)
        }
    }
}