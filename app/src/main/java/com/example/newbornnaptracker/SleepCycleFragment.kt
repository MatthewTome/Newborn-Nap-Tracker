package com.example.newbornnaptracker

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.newbornnaptracker.databinding.FragmentSleepCycleBinding

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.trackSleepButton.setOnClickListener {
            val sleepTime = binding.sleepTimeInput.text.toString()
            val recommendation = viewModel.trackSleep(sleepTime)
            binding.resultTextView.text =
                getString(R.string.next_recommended_sleep_time, recommendation)
        }
    }
}