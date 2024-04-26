package com.example.newbornnaptracker

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.Duration
import java.time.temporal.ChronoUnit

data class SleepWakeEvent(val sleepTime: LocalDateTime?, val wakeTime: LocalDateTime?)

@RequiresApi(Build.VERSION_CODES.O)
fun calculateSleepDuration(event: SleepWakeEvent): Duration {
    return Duration.between(event.sleepTime, event.wakeTime)
}

@RequiresApi(Build.VERSION_CODES.O)
fun estimateCircadianRhythm(events: List<SleepWakeEvent>, windowSize: Int): List<Pair<LocalDateTime, Duration>> {
    val estimates = mutableListOf<Pair<LocalDateTime, Duration>>()
    for (i in events.indices) {
        val event = events[i]
        val window = events.slice(maxOf(0, i - windowSize + 1)..i)
        val nonNullDurations = window.map { calculateSleepDuration(it) }

        if (nonNullDurations.isNotEmpty()) {
            val averageSleepDuration = nonNullDurations.map { it.toMillis().toDouble() }.average()

            // Add the estimate only if both sleepTime and wakeTime are not null
            if (event.sleepTime != null && event.wakeTime != null) {
                estimates.add(Pair(event.wakeTime, Duration.ofMillis(averageSleepDuration.toLong())))
            }
        }
    }
    return estimates
}

@RequiresApi(Build.VERSION_CODES.O)
fun predictNextSleepWake(estimates: List<Pair<LocalDateTime, Duration>>, lastWakeTime: LocalDateTime): Pair<LocalDateTime, LocalDateTime> {
    val lastEstimates = estimates.takeLast(3) // Consider the last 3 estimates
    val averageSleepDuration = lastEstimates.map { it.second.toMillis().toDouble() }.average()
    val predictedSleepTime = lastWakeTime.plus(averageSleepDuration.toLong(), ChronoUnit.MILLIS)
    val predictedWakeTime = predictedSleepTime.plus(averageSleepDuration.toLong(), ChronoUnit.MILLIS)
    return Pair(predictedSleepTime, predictedWakeTime)
}