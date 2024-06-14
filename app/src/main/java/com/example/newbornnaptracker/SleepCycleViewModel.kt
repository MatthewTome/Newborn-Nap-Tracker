package com.example.newbornnaptracker

import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.util.*

class SleepCycleViewModel : ViewModel() {
    fun trackSleep(lastSleepTime: String): List<String> {
        val inputFormat = SimpleDateFormat("h:mma", Locale.US)
        val outputFormat = SimpleDateFormat("h:mm a", Locale.US)

        try {
            val date = inputFormat.parse(lastSleepTime)
            val calendar = Calendar.getInstance()
            calendar.time = date!!

            val sleepTimes = mutableListOf<String>()

            for (i in 0 until 3) {
                calendar.add(Calendar.MINUTE, if (i == 0) 90 else 180)
                sleepTimes.add(outputFormat.format(calendar.time))
            }

            return sleepTimes
        } catch (e: Exception) {
            return listOf("Invalid time format")
        }
    }
}