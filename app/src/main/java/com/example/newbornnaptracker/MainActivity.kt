package com.example.newbornnaptracker

// Default
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.newbornnaptracker.databinding.ActivityMainBinding

// Spotify
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote

import com.spotify.protocol.client.Subscription
import com.spotify.protocol.types.PlayerState
import com.spotify.protocol.types.Track

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var imageView: ImageView
    private lateinit var openMusicPlayerButton: Button

//    // Spotify
//    private val clientId = "0b80095587c94433adf7511d4463ee4e"
//    private val redirectUri = "http://localhost:8888/callback"
//    private var spotifyAppRemote: SpotifyAppRemote? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        imageView = findViewById(R.id.imageView)

        openMusicPlayerButton = findViewById(R.id.open_music_player_button)
        openMusicPlayerButton.setOnClickListener {
            val intent = Intent(this, MusicPlayerActivity::class.java)
            startActivity(intent)
        }

//        val toggleImageButton = findViewById<Button>(R.id.toggleImageButton)
//        toggleImageButton.setOnClickListener {
//            toggleImageVisibility()
//        }
    }

//    private fun toggleImageVisibility() {
//        if (imageView.visibility == View.VISIBLE) {
//            imageView.visibility = View.GONE
//        } else {
//            imageView.visibility = View.VISIBLE
//        }
//    }

//    // Spotify
//    override fun onStart() {
//        super.onStart()
////        spotifyAuthManager.startAuthenticationFlow()
//        val connectionParams = ConnectionParams.Builder(clientId)
//            .setRedirectUri(redirectUri)
//            .showAuthView(true)
//            .build()
//
//        SpotifyAppRemote.connect(this, connectionParams, object : Connector.ConnectionListener {
//            override fun onConnected(appRemote: SpotifyAppRemote) {
//                spotifyAppRemote = appRemote
//                Log.d("SpotifyTest", "Connected! Yay!")
//                // Now you can start interacting with App Remote
//                connected()
//            }
//
//            override fun onFailure(throwable: Throwable) {
//                Log.e("SpotifyTest", throwable.message, throwable)
//                // Something went wrong when attempting to connect! Handle errors here
//            }
//        })
//    }
//
//    private fun connected() {
//        spotifyAppRemote?.let {
//            // Play a playlist
//            val playlistURI = "spotify:playlist:37i9dQZF1DX2sUQwD7tbmL"
//            it.playerApi.play(playlistURI)
//            // Subscribe to PlayerState
//            it.playerApi.subscribeToPlayerState().setEventCallback {
//                val track: Track = it.track
//                Log.d("SpotifyTest", track.name + " by " + track.artist.name)
//            }
//        }
//    }
//
//    override fun onStop() {
//        super.onStop()
//        spotifyAppRemote?.let {
//            SpotifyAppRemote.disconnect(it)
//        }
//    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}