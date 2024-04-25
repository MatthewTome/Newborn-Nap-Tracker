package com.example.newbornnaptracker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.newbornnaptracker.databinding.FragmentFirstBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}



//package com.example.newbornnaptracker
//
//import android.os.Bundle
//import androidx.fragment.app.Fragment
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
////import android.webkit.WebView
//import androidx.navigation.fragment.findNavController
//import com.example.newbornnaptracker.databinding.FragmentFirstBinding
//
///**
// * A simple [Fragment] subclass as the default destination in the navigation.
// */
//class FirstFragment : Fragment() {
//
//    private var _binding: FragmentFirstBinding? = null
//
//    // This property is only valid between onCreateView and
//    // onDestroyView.
//    private val binding get() = _binding!!
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//
//        _binding = FragmentFirstBinding.inflate(inflater, container, false)
//
//        // Spotify
////        // Create a WebView instance
////        val webView = WebView(requireContext())
////        webView.layoutParams = ViewGroup.LayoutParams(
////            ViewGroup.LayoutParams.MATCH_PARENT,
////            ViewGroup.LayoutParams.MATCH_PARENT
////        )
////
////        // Add the WebView to the layout
////        binding.root.addView(webView)
////
////        // Load the HTML content
////        val htmlContent = """
////        <!DOCTYPE html>
////        <html>
////        <head>
////        <title>Spotify Web Playback SDK Quick Start</title>
////        </head>
////        <body>
////        <h1>Spotify Web Playback SDK Quick Start</h1>
////        <button id="togglePlay">Toggle Play</button>
////        <script src="https://sdk.scdn.co/spotify-player.js"></script>
////        <script>
////        window.onSpotifyWebPlaybackSDKReady = () => {
////        const token = '[My access token]';
////        const player = new Spotify.Player({
////            name: 'Web Playback SDK Quick Start Player',
////            getOAuthToken: cb => { cb(token); },
////            volume: 0.5
////        });
////
////        // Ready
////        player.addListener('ready', ({ device_id }) => {
////            console.log('Ready with Device ID', device_id);
////        });
////
////        // Not Ready
////        player.addListener('not_ready', ({ device_id }) => {
////            console.log('Device ID has gone offline', device_id);
////        });
////
////        player.addListener('initialization_error', ({ message }) => {
////            console.error(message);
////        });
////
////        player.addListener('authentication_error', ({ message }) => {
////            console.error(message);
////        });
////
////        player.addListener('account_error', ({ message }) => {
////            console.error(message);
////        });
////
////        document.getElementById('togglePlay').onclick = function() {
////            player.togglePlay();
////        };
////
////        player.connect();
////        }
////        </script>
////        </body>
////        </html>
////    """
////
////        webView.loadDataWithBaseURL(null, htmlContent, "text/html", "utf-8", null)
//
//        return binding.root
//
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        binding.buttonFirst.setOnClickListener {
//            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
//        }
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
////        _binding?.root?.removeAllViews()
//        _binding = null
//    }
//}