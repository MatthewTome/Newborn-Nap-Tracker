package com.example.newbornnaptracker

import android.os.Build
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.example.newbornnaptracker.databinding.FragmentSleepCycleBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class SleepCycleFragment : Fragment() {

    private lateinit var binding: FragmentSleepCycleBinding
    private val viewModel: SleepCycleViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSleepCycleBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.estimatesLiveData.observe(viewLifecycleOwner) { estimates ->
            val estimatesText = estimates.joinToString("\n") { (dateTime, duration) ->
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                "${dateTime.format(formatter)}: $duration"
            }
            binding.estimatesTextView.text = "Estimates:\n$estimatesText"
        }

        viewModel.predictionLiveData.observe(viewLifecycleOwner) { (predictedSleepTime, predictedWakeTime) ->
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
            binding.predictionsTextView.text = "Predictions:\nSleep Time: ${predictedSleepTime.format(formatter)}\nWake Time: ${predictedWakeTime.format(formatter)}"
        }

        binding.addSleepButton.setOnClickListener {
            val sleepTime = LocalDateTime.now()
            viewModel.addSleepWakeEvent(SleepWakeEvent(sleepTime, null))
        }

        binding.addWakeButton.setOnClickListener {
            val wakeTime = LocalDateTime.now()
            viewModel.addSleepWakeEvent(SleepWakeEvent(null, wakeTime))
        }
    }
}