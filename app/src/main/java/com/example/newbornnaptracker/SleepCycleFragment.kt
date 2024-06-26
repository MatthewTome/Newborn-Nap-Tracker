package com.example.newbornnaptracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.newbornnaptracker.databinding.FragmentSleepCycleBinding
import android.text.Html
import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import java.util.Locale

class SleepCycleFragment : Fragment() {

    private var _binding: FragmentSleepCycleBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SleepCycleViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.all { it.value }) {
            viewModel.addToCalendar(requireContext())
            Toast.makeText(context, "Added to calendar", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Calendar permissions are required to add events", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSleepCycleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the hour picker
        binding.hourPicker.minValue = 1
        binding.hourPicker.maxValue = 12
        binding.hourPicker.wrapSelectorWheel = true

        // Initialize the minute picker
        binding.minutePicker.minValue = 0
        binding.minutePicker.maxValue = 59
        binding.minutePicker.wrapSelectorWheel = true
        binding.minutePicker.setFormatter { i -> String.format(Locale.US, "%02d", i) }

        // Initialize the AM/PM picker
        val amPmValues = arrayOf("AM", "PM")
        binding.amPmPicker.minValue = 0
        binding.amPmPicker.maxValue = amPmValues.size - 1
        binding.amPmPicker.displayedValues = amPmValues
        binding.amPmPicker.wrapSelectorWheel = true

        // Handle the change in hour to update AM/PM
        binding.hourPicker.setOnValueChangedListener { _, oldVal, newVal ->
            if (oldVal == 12 && newVal == 1) {
                binding.amPmPicker.value = (binding.amPmPicker.value + 1) % 2
            } else if (oldVal == 1 && newVal == 12) {
                binding.amPmPicker.value = (binding.amPmPicker.value + 1) % 2
            }
        }

        binding.trackSleepButton.setOnClickListener {
            val hour = binding.hourPicker.value
            val minute = binding.minutePicker.value
            val period = amPmValues[binding.amPmPicker.value]
            val sleepTime = String.format(Locale.US, "%d:%02d%s", hour, minute, period)
            val recommendations = viewModel.trackSleep(sleepTime)
            val selectedBabyIndex = sharedViewModel.selectedBabyIndex.value ?: 0
            sharedViewModel.updateSleepRecommendations(selectedBabyIndex, recommendations)

            displayRecommendations(recommendations)

            if (checkCalendarPermissions()) {
                viewModel.addToCalendar(requireContext())
                Toast.makeText(context, "Sleep time added to calendar.", Toast.LENGTH_LONG).show()
            } else {
                requestCalendarPermissions()
            }
        }

        setupBabyRadioButtons()
    }

    private fun setupBabyRadioButtons() {
        val radioGroup = binding.babyRadioGroup
        radioGroup.removeAllViews()
        sharedViewModel.babyNames.observe(viewLifecycleOwner) { babyNames ->
            babyNames.forEachIndexed { index, name ->
                val radioButton = RadioButton(context).apply {
                    text = name
                    id = View.generateViewId()
                    setOnClickListener {
                        sharedViewModel.setSelectedBabyIndex(index)
                    }
                }
                radioGroup.addView(radioButton)
                if (index == 0) {
                    radioButton.isChecked = true
                    sharedViewModel.setSelectedBabyIndex(index)
                }
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
        requestPermissionLauncher.launch(
            arrayOf(
                Manifest.permission.WRITE_CALENDAR,
                Manifest.permission.READ_CALENDAR
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}