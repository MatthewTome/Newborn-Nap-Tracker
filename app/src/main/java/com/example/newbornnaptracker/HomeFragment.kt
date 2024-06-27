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

        sharedViewModel.sleepRecommendations.observe(viewLifecycleOwner) { recommendations ->
            displayRecommendations(recommendations)
        }
    }

    private fun displayRecommendations(recommendationsMap: Map<Int, List<String>>) {
        val numBabies = sharedViewModel.numBabies.value ?: 0
        val babyNames = sharedViewModel.babyNames.value ?: emptyList()
        val allRecommendations = StringBuilder()

        for (i in 0 until numBabies) {
            allRecommendations.append("<h2>${babyNames[i]}</h2>")
            val recommendations = recommendationsMap[i] ?: emptyList()
            if (recommendations.isNotEmpty()) {
                val formattedRecommendations = recommendations.mapIndexed { index, time ->
                    val boldTime = "<b>$time</b>"
                    when (index) {
                        0 -> "Next nap: $boldTime"
                        1 -> "Then: $boldTime"
                        2 -> "Finally: $boldTime"
                        else -> ""
                    }
                }
                allRecommendations.append(formattedRecommendations.joinToString("<br>")).append("<br><br>")
                if (i < numBabies - 1) {
                    allRecommendations.append("<hr>")
                }
            } else {
                allRecommendations.append(getString(R.string.no_naps_scheduled)).append("<br><br>")
            }
        }

        binding.textviewFirst.text = Html.fromHtml(allRecommendations.toString(), Html.FROM_HTML_MODE_COMPACT)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
