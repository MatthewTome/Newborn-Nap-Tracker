package com.example.newbornnaptracker

import android.content.ContentUris
import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.util.*
import android.content.ContentValues
import android.content.Context
import android.provider.CalendarContract
import android.util.Log

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

    fun addToCalendar(context: Context) {
        SimpleDateFormat("h:mma", Locale.US)

        val calendarId = getDefaultCalendarId(context)
        if (calendarId == -1L) {
            Log.e("SleepCycleViewModel", "No suitable calendar found")
            return
        }

        val currentDate = Date() // current date and time
        val startMillis = currentDate.time
        val endMillis = startMillis + 60 * 60 * 1000 // 1 hour duration

        val values = ContentValues().apply {
            put(CalendarContract.Events.DTSTART, startMillis)
            put(CalendarContract.Events.DTEND, endMillis)
            put(CalendarContract.Events.TITLE, "Baby's Sleep Time (App Test)")
            put(CalendarContract.Events.DESCRIPTION, "Recorded sleep time for baby from NewbornNapTracker app")
            put(CalendarContract.Events.CALENDAR_ID, calendarId)
            put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().id)
            put(CalendarContract.Events.HAS_ALARM, 1) // Add a reminder
            put(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY)
        }

        try {
            val uri = context.contentResolver.insert(CalendarContract.Events.CONTENT_URI, values)
            uri?.let {
                // Add a reminder
                val reminderValues = ContentValues().apply {
                    put(CalendarContract.Reminders.MINUTES, 5) // Reminder 5 minutes before
                    put(CalendarContract.Reminders.EVENT_ID, ContentUris.parseId(it))
                    put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT)
                }
                context.contentResolver.insert(CalendarContract.Reminders.CONTENT_URI, reminderValues)
            }
            Log.d("SleepCycleViewModel", "Event added successfully")
        } catch (e: SecurityException) {
            Log.e("SleepCycleViewModel", "Permission denied: ${e.message}")
        } catch (e: Exception) {
            Log.e("SleepCycleViewModel", "Error adding event: ${e.message}")
        }
    }

    private fun getDefaultCalendarId(context: Context): Long {
        val projection = arrayOf(
            CalendarContract.Calendars._ID,
            CalendarContract.Calendars.ACCOUNT_NAME
        )
        val selection = "(${CalendarContract.Calendars.IS_PRIMARY} = 1) AND (${CalendarContract.Calendars.ACCOUNT_TYPE} = ?)"
        val selectionArgs = arrayOf("com.google")
        try {
            context.contentResolver.query(
                CalendarContract.Calendars.CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                null
            )?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val idColumn = cursor.getColumnIndex(CalendarContract.Calendars._ID)
                    val nameColumn = cursor.getColumnIndex(CalendarContract.Calendars.ACCOUNT_NAME)
                    Log.d("SleepCycleViewModel", "Using calendar: ${cursor.getString(nameColumn)}")
                    return cursor.getLong(idColumn)
                }
            }
        } catch (e: SecurityException) {
            Log.e("SleepCycleViewModel", "Permission denied: ${e.message}")
        }
        return -1 // Default calendar not found
    }
}