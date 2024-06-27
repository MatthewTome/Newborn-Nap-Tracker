package com.example.newbornnaptracker

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.newbornnaptracker.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedViewModel.numBabies.observe(viewLifecycleOwner) {
            updateDisplay()
        }

        sharedViewModel.babyNames.observe(viewLifecycleOwner) {
            updateDisplay()
        }

        sharedViewModel.sleepPredictions.observe(viewLifecycleOwner) {
            updateDisplay()
        }
    }

    private fun updateDisplay() {
        val numBabies = sharedViewModel.numBabies.value ?: 0
        val babyNames = sharedViewModel.babyNames.value ?: emptyList()
        val predictionsMap = sharedViewModel.sleepPredictions.value ?: emptyMap()

        if (numBabies == 0 || babyNames.isEmpty()) {
            binding.titleTextView.text = Html.fromHtml(getString(R.string.intro_message), Html.FROM_HTML_MODE_COMPACT)
            binding.textviewFirst.text = getString(R.string.empty)
            return
        }

        if (predictionsMap.isEmpty()) {
            binding.titleTextView.text = Html.fromHtml(getString(R.string.no_nap_predictions), Html.FROM_HTML_MODE_COMPACT)
            binding.textviewFirst.text = getString(R.string.empty)
            return
        }

        binding.titleTextView.text = Html.fromHtml(getString(R.string.nap_predictions), Html.FROM_HTML_MODE_COMPACT)
        displayPredictions(numBabies, babyNames, predictionsMap)
    }

    private fun displayPredictions(numBabies: Int, babyNames: List<String>, predictionsMap: Map<Int, List<String>>) {
        val allPredictions = StringBuilder()

        for (i in 0 until numBabies) {
            val babyName = "<b>${babyNames.getOrElse(i) { "Baby $i" }}</b><br>"
            allPredictions.append(babyName)

            val predictions = predictionsMap[i] ?: emptyList()

            if (predictions.isNotEmpty()) {
                val formattedPredictions = predictions.mapIndexed { index, time ->
                    val boldTime = "<b>$time</b>"
                    when (index) {
                        0 -> "Next nap: $boldTime<br>"
                        1 -> "Then: $boldTime<br>"
                        2 -> "Finally: $boldTime<br>"
                        else -> ""
                    }
                }
                formattedPredictions.forEach {
                    allPredictions.append(it)
                }
                if (i < numBabies - 1) {
                    allPredictions.append("<br>------------------------<br><br>")
                }
            } else {
                allPredictions.append(getString(R.string.no_naps_scheduled)).append("<br><br>")
            }
        }

        val finalText = allPredictions.toString()
        binding.textviewFirst.text = Html.fromHtml(finalText, Html.FROM_HTML_MODE_COMPACT)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}