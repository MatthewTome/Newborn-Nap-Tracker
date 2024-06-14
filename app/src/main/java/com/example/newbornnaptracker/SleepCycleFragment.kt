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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}