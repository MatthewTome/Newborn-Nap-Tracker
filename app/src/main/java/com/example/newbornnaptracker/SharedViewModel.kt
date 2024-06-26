package com.example.newbornnaptracker

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    // Sleep Cycle
    private val _sleepRecommendations = MutableLiveData<Map<Int, List<String>>>()
    val sleepRecommendations: LiveData<Map<Int, List<String>>> = _sleepRecommendations

    fun updateSleepRecommendations(babyIndex: Int, recommendations: List<String>) {
        val currentRecommendations = _sleepRecommendations.value?.toMutableMap() ?: mutableMapOf()
        currentRecommendations[babyIndex] = recommendations
        _sleepRecommendations.value = currentRecommendations
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

    // Settings data
    private val _numBabies = MutableLiveData<Int>()
    val numBabies: LiveData<Int> get() = _numBabies

    private val _babyNames = MutableLiveData<List<String>>(listOf())
    val babyNames: LiveData<List<String>> get() = _babyNames

    private val _babyAges = MutableLiveData<List<Int>>(listOf())
    val babyAges: LiveData<List<Int>> get() = _babyAges

    private val _selectedBabyIndex = MutableLiveData<Int>()
    val selectedBabyIndex: LiveData<Int> get() = _selectedBabyIndex

    fun setNumBabies(numBabies: Int) {
        _numBabies.value = numBabies
    }

    fun setBabyNames(names: List<String>) {
        _babyNames.value = names
    }

    fun setBabyAges(ages: List<Int>) {
        _babyAges.value = ages
    }

    fun setSelectedBabyIndex(index: Int) {
        _selectedBabyIndex.value = index
    }
}