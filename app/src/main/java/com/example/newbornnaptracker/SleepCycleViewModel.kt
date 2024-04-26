package com.example.newbornnaptracker

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.time.Duration
import java.time.LocalDateTime

class SleepCycleViewModel : ViewModel() {
    private val sleepWakeEvents = mutableListOf<SleepWakeEvent>()
    private val estimates = mutableListOf<Pair<LocalDateTime, Duration>>()

    private val _estimatesLiveData = MutableLiveData<List<Pair<LocalDateTime, Duration>>>()
    val estimatesLiveData: LiveData<List<Pair<LocalDateTime, Duration>>> = _estimatesLiveData

    private val _predictionLiveData = MutableLiveData<Pair<LocalDateTime, LocalDateTime>>()
    val predictionLiveData: LiveData<Pair<LocalDateTime, LocalDateTime>> = _predictionLiveData

    @RequiresApi(Build.VERSION_CODES.O)
    fun addSleepWakeEvent(event: SleepWakeEvent) {
        sleepWakeEvents.add(event)
        updateEstimates()
        updatePrediction()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateEstimates() {
        estimates.clear()
        estimates.addAll(estimateCircadianRhythm(sleepWakeEvents, 3))
        _estimatesLiveData.value = estimates
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updatePrediction() {
        val lastWakeTime = sleepWakeEvents.lastOrNull()?.wakeTime ?: return
        val prediction = predictNextSleepWake(estimates, lastWakeTime)
        _predictionLiveData.value = prediction
    }
}