package com.example.newbornnaptracker

import android.content.Intent
import android.os.Bundle
import android.provider.CalendarContract
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.newbornnaptracker.databinding.FragmentSecondBinding
import java.util.Calendar

class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSecond.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }

        // Set the initial date for the calendar
        binding.calendarView.date = System.currentTimeMillis()

        // Set the OnDateChangeListener to handle date selection
        binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->

            // Add an event to the calendar
            val eventTitle = "My Event"
            val eventStartTime = Calendar.getInstance().apply {
                set(year, month, dayOfMonth, 10, 0) // 10:00 AM
            }
            val eventEndTime = Calendar.getInstance().apply {
                set(year, month, dayOfMonth, 11, 0) // 11:00 AM
            }

            val intent = Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.Events.TITLE, eventTitle)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, eventStartTime.timeInMillis)
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, eventEndTime.timeInMillis)

            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}