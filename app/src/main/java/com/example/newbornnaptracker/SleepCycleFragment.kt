package com.example.newbornnaptracker

import androidx.fragment.app.viewModels
import androidx.fragment.app.activityViewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.newbornnaptracker.databinding.FragmentSleepCycleBinding
import android.text.Html
import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat

class SleepCycleFragment : Fragment() {

    private var _binding: FragmentSleepCycleBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SleepCycleViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSleepCycleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.trackSleepButton.setOnClickListener {
            val sleepTime = binding.sleepTimeInput.text.toString()
            val recommendations = viewModel.trackSleep(sleepTime)
            displayRecommendations(recommendations)

            if (checkCalendarPermissions()) {
                viewModel.addToCalendar(requireContext())
                Toast.makeText(context, "Sleep time added to calendar.", Toast.LENGTH_LONG).show()
                Log.d("SleepCycleFragment", "Attempted to add sleep time: $sleepTime")
            } else {
                requestCalendarPermissions()
            }
        }
    }

    private fun displayRecommendations(recommendations: List<String>) {
        if (recommendations.size == 1 && recommendations[0] == "Invalid time format") {
            binding.resultTextView.text = getString(R.string.invalid_time_format)
        } else {
            val formattedRecommendations = recommendations.mapIndexed { index, time ->
                val boldTime = "<b>$time</b>"
                val exclamation = if (index == 0) "!" else ""
                when (index) {
                    0 -> getString(R.string.next_recommended_sleep_time, boldTime) + exclamation
                    1 -> getString(R.string.next_predicted_sleep_time, boldTime)
                    2 -> getString(R.string.and_then_sleep_time, boldTime)
                    else -> ""
                }
            }
            binding.resultTextView.text = Html.fromHtml(formattedRecommendations.joinToString("<br><br>"), Html.FROM_HTML_MODE_COMPACT)
            sharedViewModel.updateSleepRecommendations(recommendations)
        }
    }

    private fun checkCalendarPermissions(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.WRITE_CALENDAR
        ) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_CALENDAR
                ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestCalendarPermissions() {
        requestPermissions(
            arrayOf(
                Manifest.permission.WRITE_CALENDAR,
                Manifest.permission.READ_CALENDAR
            ),
            CALENDAR_PERMISSION_REQUEST_CODE
        )
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            CALENDAR_PERMISSION_REQUEST_CODE -> {
                if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                    // All permissions were granted, add event to calendar
                    val sleepTime = binding.sleepTimeInput.text.toString()
                    viewModel.addToCalendar(requireContext())
                    Toast.makeText(context, "Sleep time added to calendar", Toast.LENGTH_SHORT).show()
                } else {
                    // Permission denied, show a message to the user
                    Toast.makeText(context, "Calendar permissions are required to add events", Toast.LENGTH_LONG).show()
                }
                return
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val CALENDAR_PERMISSION_REQUEST_CODE = 100
    }
}