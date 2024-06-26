package com.example.newbornnaptracker

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    // Sleep Cycle
    private val _sleepRecommendations = MutableLiveData<List<String>>()
    val sleepRecommendations: LiveData<List<String>> = _sleepRecommendations

    fun updateSleepRecommendations(recommendations: List<String>) {
        _sleepRecommendations.value = recommendations
    }

    // Music Player
    private val _resourceId = MutableLiveData<Int>()
    val resourceId: LiveData<Int> get() = _resourceId

    private val _isPlaying = MutableLiveData<Boolean>()
    val isPlaying: LiveData<Boolean> get() = _isPlaying

    fun setResourceId(id: Int) {
        _resourceId.value = id
    }

    fun setPlaying(isPlaying: Boolean) {
        _isPlaying.value = isPlaying
    }
}