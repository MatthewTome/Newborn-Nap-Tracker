package com.example.newbornnaptracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.newbornnaptracker.databinding.FragmentFirstBinding
import androidx.fragment.app.activityViewModels
import android.text.Html

class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonAddNap.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }

        binding.buttonSleepCycle.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SleepCycleFragment)
        }

        sharedViewModel.sleepRecommendations.observe(viewLifecycleOwner) { recommendations ->
            displayRecommendations(recommendations)
        }
    }

    private fun displayRecommendations(recommendations: List<String>) {
        if (recommendations.isNotEmpty()) {
            val header = "<b>Your baby's predicted nap times for today:</b>"
            val formattedRecommendations = recommendations.mapIndexed { index, time ->
                val boldTime = "<b>$time</b>"
                when (index) {
                    0 -> "Next nap: $boldTime"
                    1 -> "Then: $boldTime"
                    2 -> "Finally: $boldTime"
                    else -> ""
                }
            }
            val fullText = listOf(header) + formattedRecommendations
            binding.textviewFirst.text = Html.fromHtml(fullText.joinToString("<br><br>"), Html.FROM_HTML_MODE_COMPACT)
        } else {
            binding.textviewFirst.text = getString(R.string.no_naps_scheduled)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}