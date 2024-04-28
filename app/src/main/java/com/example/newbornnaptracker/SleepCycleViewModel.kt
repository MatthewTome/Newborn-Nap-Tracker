package com.example.newbornnaptracker

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.time.Duration
import java.time.LocalDateTime

class SleepCycleViewModel : ViewModel() {
    fun trackSleep(lastSleepTime: String): String {
        val lastSleepTimeHours = lastSleepTime.substring(0, lastSleepTime.length - 2).toIntOrNull() ?: 0
        val lastSleepTimeAmPm = lastSleepTime.substring(lastSleepTime.length - 2)

        var nextSleepTimeHours = lastSleepTimeHours + 4
        var nextSleepTimeAmPm = if (lastSleepTimeAmPm.equals("AM", ignoreCase = true)) "PM" else "AM"

        if (nextSleepTimeHours >= 12) {
            nextSleepTimeHours -= 12
            nextSleepTimeAmPm = if (nextSleepTimeAmPm == "AM") "PM" else "AM"
        }

        if (nextSleepTimeHours == 0) {
            nextSleepTimeHours = 12
        }

        val formattedNextSleepTimeHours = nextSleepTimeHours.toString().padStart(2, '0')

        return "$formattedNextSleepTimeHours:00 $nextSleepTimeAmPm"
    }
}