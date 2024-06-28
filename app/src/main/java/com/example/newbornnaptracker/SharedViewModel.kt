package com.example.newbornnaptracker

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    // Sleep Cycle
    private val _sleepPredictions = MutableLiveData<Map<Int, List<String>>>()
    val sleepPredictions: LiveData<Map<Int, List<String>>> = _sleepPredictions

    fun updateSleepPredictions(babyIndex: Int, predictions: List<String>) {
        val currentPredictions = _sleepPredictions.value?.toMutableMap() ?: mutableMapOf()
        currentPredictions[babyIndex] = predictions
        _sleepPredictions.value = currentPredictions
    }

    // Sound Machine
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

    // Settings
    private val _numBabies = MutableLiveData<Int>()
    val numBabies: LiveData<Int> get() = _numBabies

    private val _babyNames = MutableLiveData<List<String>>(listOf())
    val babyNames: LiveData<List<String>> get() = _babyNames

    private val _selectedBabyIndex = MutableLiveData<Int>()
    val selectedBabyIndex: LiveData<Int> get() = _selectedBabyIndex

    fun setBabyNames(names: List<String>) {
        _babyNames.value = names
        _numBabies.value = names.size
    }

    fun setSelectedBabyIndex(index: Int) {
        _selectedBabyIndex.value = index
    }
}