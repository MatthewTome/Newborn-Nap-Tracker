package com.example.newbornnaptracker

interface MusicPlayerControlListener {
    fun playSound()
    fun pauseSound()
    fun stopSound()
    fun setSound(resourceId: Int)
    fun isPlaying(): Boolean
}